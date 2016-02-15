##FrameAnimation 逐帧动画（AnimationDrawable）

- 将每一帧的图片放在资源文件夹`res/drawable`
- 在XML中定义动画每一帧及其持续时间
- 在Java代码中加载动画并设置给View，然后启动动画
- OneShot属性为true则只播放一次，否则不断循环播放

```
<?xml version="1.0" encoding="utf-8"?>
<animation-list xmlns:android="http://schemas.android.com/apk/res/android"
   android:oneshot="false">
   <item
       android:drawable="@drawable/icon1"
       android:duration="200" />
   <item
       android:drawable="@drawable/icon2"
       android:duration="200" />
   <item
       android:drawable="@drawable/icon3"
       android:duration="200" />
   <item
       android:drawable="@drawable/icon4"
       android:duration="200" />
</animation-list>
```

```
mFrameAnimation = (AnimationDrawable) getResources().getDrawable(R.drawable.frame_anim); // 从XML加载动画
mTextView.setBackground(mFrameAnimation);
mFrameAnimation.start();
```

- 也可以在Java代码中实例化AnimationDrawable对象，并添加帧和持续时间

```
mFrameAnimation = new AnimationDrawable();
mFrameAnimation.addFrame(getResources().getDrawable(R.drawable.icon1), 200);
mFrameAnimation.addFrame(getResources().getDrawable(R.drawable.icon2), 200);
```

##Animation（补间动画 / Tween Animation）

```
<?xml version="1.0" encoding="utf-8"?>
<rotate xmlns:android="http://schemas.android.com/apk/res/android"
   android:duration="1000"
   android:fromDegrees="0"
   android:interpolator="@android:interpolator/linear"
   android:pivotX="50%"
   android:pivotY="50%"
   android:repeatCount="infinite"
   android:repeatMode="restart"
   android:toDegrees="360" />
```

```
Animation anim = AnimationUtils.loadAnimation(this, R.anim.animation);
mTextView.startAnimation(anim);
```

1. 从XML加载Animation，并设置给View
2. RotateAnimation，旋转动画
3. 属性：
 - from-to属性
 - duration 动画持续时间，单位是ms
 - pivotX, pivotY 指定旋转的中心点。`pivotX="50%"`：`50%`表示元素自身尺寸的`50%`, `50%p`表示父元素尺寸的`50%`。
  **注意：这里指定的中心点坐标是相对坐标原点而言的，而坐标原点在View的左上角。**
  假设想让View沿着父视图的中心旋转，由于坐标原点在View左上角，而不是父视图左上角，所以写成`50%p`是不能正常工作的（除非两者左上角重合）。可以在Java代码中进行计算。
 - repeatCount, repeatMode
  repeatMode : reverse 反转； restart 重新开始
  repeatCount : 5 循环五次；infinite 无限循环
4. interpolator 插值器

##AnimationSet

```
<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android"
   android:duration="2000"
   android:fillAfter="true">

   <translate
       android:fromXDelta="0"
       android:fromYDelta="-50%p"
       android:interpolator="@android:interpolator/accelerate_decelerate"
       android:toXDelta="0"
       android:toYDelta="0" />

   <alpha
       android:fromAlpha="0.5"
       android:startOffset="2000"
       android:toAlpha="1" />

   <scale
       android:fromXScale="1"
       android:fromYScale="1"
       android:pivotX="50%"
       android:pivotY="50%"
       android:startOffset="4000"
       android:toXScale="2"
       android:toYScale="2" />
</set>
```

```
// 返回的是AnimationSet实例，AnimationSet继承自Animation
Animation anim = AnimationUtils.loadAnimation(this, R.anim.animation_set);
mTextView.startAnimation(anim);
```

1. AnimationSet extends Animation
2. TranslateAnimation / AlphaAnimation / ScaleAnimation
3. startOffset：默认set中所有动画是同时播放的，如果有设置startOffset，那一项就会延时播放。这样就可以实现一系列的动画效果了。
4. fillAfter：设置为true时，动画播放完成后停留在最后一帧，而不是复位到初始位置
6. `android:fromYDelta="-50%p"` 坐标原点还是左上角，但由于TranslateAnimation是平移运动，这里就可以直接用这种方式实现从父视图的上方中心位置开始移动。
7. **注意：**`android:fromAlpha="0.5"` 虽然Alpha动画在2s时才开始执行，但是在0s的时候，View的Alpha值就已经被设置成fromAlpha的数值了。类似的，如果设置了Scale的初始值不为1，整个View的坐标比例也会发生变化。例如初始Scale为0.5，则50%p实际上只有父视图的25%。
8. AnimationSet继承自Animation，但它继承的一些属性比较特别。其中的一些属性，会直接被设置给它包含的每个Animation；而另外一些属性会被忽略；还有一些属性会被应用到AnimationSet本身。
 > 
 - duration, repeatMode, fillBefore, fillAfter: These properties, when set on an AnimationSet object, will be pushed down to all child animations.
 - repeatCount, fillEnabled: These properties are ignored for AnimationSet.
 - startOffset, shareInterpolator: These properties apply to the AnimationSet itself.


##使用Java代码实例化Animation

```
Animation anim = new TranslateAnimation(
       Animation.RELATIVE_TO_PARENT, -0.2f, Animation.RELATIVE_TO_PARENT, -0.2f,
       Animation.RELATIVE_TO_PARENT, -0.5f, Animation.RELATIVE_TO_PARENT, 0
);
anim.setFillAfter(true);
anim.setDuration(2000);
mTextView.setAnimation(anim);
```

##CustomAnimation CustomIntepolator

###自定义Animation

```
public class MyAnimation extends Animation {

   private float mFromXValue = 0.0f;
   private float mFromYValue = 0.0f;
   private float mFromXDelta;
   private float mFromYDelta;

   public MyAnimation(float fromXDelta, float fromYDelta) {
       mFromXValue = fromXDelta;
       mFromYValue = fromYDelta;
   }

   @Override
   public void initialize(int width, int height, int parentWidth, int parentHeight) {
       super.initialize(width, height, parentWidth, parentHeight);
       mFromXDelta = parentWidth * mFromXValue;
       mFromYDelta = parentHeight * mFromYValue;
   }

   /**
    * @param interpolatedTime 0~1f
    * @param t
    */
   @Override
   protected void applyTransformation(float interpolatedTime, Transformation t) {
       float dx = mFromXDelta + ((0 - mFromXDelta) * interpolatedTime);
       float dy = mFromYDelta + ((0 - mFromYDelta) * interpolatedTime);
       final Matrix matrix = t.getMatrix();
       // pre-方法向前生长，post-方法向后生长，set-方法先清空已有变换，再进行变换
       matrix.setTranslate(dx, dy);
   }
}
```

1. `initialize`方法中，对坐标尺寸相关内容进行初始化
2. `applyTransformation`方法中，对矩阵`Matrix`进行变换实现动画
  - interpolatedTime参数取值为0~1f，代表当前动画执行到的位置。
3. `matrix.setTranslate(dx, dy)`，`matrix.postRotate(180)`，`matrix.preScale(scaleX, scaleY)`。pre-方法向前生长，post-方法向后生长，set-方法先清空已有变换，再进行变换。如果要进行多个变换，最多使用一个set方法。
4. 上面的代码实现了一个类似TranslateAnimation的自定义动画。

###自定义Interpolator

```
public class MyInterpolator implements Interpolator {

   float[] rawStep; // 1f, 0.5f, 0.5f, 0.25f, 0.25f, 0.125f, 0.125f, ...
   float[] normalizedStep; // 1k, 0.5k, 0.5k, ...
   float[] normalizedStepSum; // 0, 1k, (1+0.5)k, (1+0.5+0.5)k, ..., 1
   float sum;

   public MyInterpolator() {
       final int k = 10;
       rawStep = new float[2 * k + 1];
       normalizedStep = new float[rawStep.length];
       normalizedStepSum = new float[rawStep.length + 1];

       rawStep[0] = 1f;
       float f = 1f;
       for (int i = 0; i < k; ++i) {
           rawStep[2 * i] = f;
           rawStep[2 * i + 1] = f;
           f /= 2f;
       }

       sum = 0;
       for (float f1 : rawStep) {
           sum += f1;
       }

       normalizedStepSum[0] = 0;
       for (int i = 0; i < rawStep.length; ++i) {
           normalizedStep[i] = rawStep[i] / sum;
           normalizedStepSum[i + 1] = normalizedStepSum[i] + normalizedStep[i];
       }
   }

   public float getInterpolation(float input) {
       float r = 1;
       for (int i = 0; i < normalizedStep.length; ++i) {
           if (input < normalizedStepSum[i + 1]) {
               float x = (input - normalizedStepSum[i]) / normalizedStep[i]; // x = 0~1
               if (i % 2 == 0) { // down
                   return accelerate(x, r);
               } else { // up
                   return decelerate(x, r);
               }
           }
           if (i % 2 == 0) {
               r /= 2;
           }
       }
       return input;
   }

   /**
    * @param x
    * @param range
    * @return 1-range ~ 1
    */
   float accelerate(float x, float range) {
       return x * x * range + 1 - range;
   }

   /**
    * @param x
    * @param range
    * @return 1-range ~ 1
    */
   float decelerate(float x, float range) {
       return (1 - x) * (1 - x) * range + 1 - range;
   }
}
```

1. 自定义Interpolator，需要实现Interpolator接口的getInterpolation方法。这个方法相当于一个数学函数，输入值、输出值范围都是0~1。
2. 动画播放的时候，每刷新一帧就会调用一次getInterpolation方法，输入的参数从0到1线性递增；而返回值作为interpolatedTime参数传入到Animation的applyTransformation方法中。
 - LinearInterpolator的getInterpolation方法是一次函数，返回值等于输入值，所以返回值也会线性递增。
 - 而标准的AccelerateInterpolator，则是对输入值求平方，所以就会有加速的效果。
3. 上面的代码中，将整个动画播放过程进行了分段，每段进行加速或者减速，最后配合自定义的TranslateAnimation，实现了View下落弹跳的效果。

###AnimationListener 监听器

```
Animation anim = new MyAnimation(0f, -0.5f);
anim.setInterpolator(new MyInterpolator());
anim.setDuration(8000);
anim.setFillAfter(true);
anim.setAnimationListener(new Animation.AnimationListener() {
   @Override
   public void onAnimationStart(Animation animation) {
       mTextView.setText("Animation");
   }

   @Override
   public void onAnimationEnd(Animation animation) {
       mTextView.setText("Hello World");
   }

   @Override
   public void onAnimationRepeat(Animation animation) {

   }
});
mTextView.startAnimation(anim);
```

##LayoutAnimation，用于ViewGroup

###XML方式实现

定义每个ChildView的动画

```
<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android"
   android:duration="500">

   <scale
       android:fromXScale="0"
       android:fromYScale="0"
       android:pivotX="50%"
       android:pivotY="50%"
       android:toXScale="1"
       android:toYScale="1" />

   <alpha
       android:fromAlpha="0"
       android:toAlpha="1" />
</set>
```

定义LayoutAnimation

```
<?xml version="1.0" encoding="utf-8"?>
<layoutAnimation xmlns:android="http://schemas.android.com/apk/res/android"
   android:animation="@anim/layout_animation_item"
   android:animationOrder="normal"
   android:delay="0.15" />
```

在ViewGroup中应用LayoutAnimation

```
<LinearLayout android:layoutAnimation="@anim/layout_animation" >
</LinearLayout>
```

ViewGroup加载时，按照animationOrder属性指定的顺序（顺序、倒序、随机），delay属性指定的时延，每个ChildView依次显示动画。

###Java实现

```
Animation anim = AnimationUtils.loadAnimation(this, R.anim.layout_animation_item);

LayoutAnimationController lac = new LayoutAnimationController(anim);
lac.setOrder(LayoutAnimationController.ORDER_REVERSE);
lac.setDelay(1);

mListView.setLayoutAnimation(lac);
```

##Animator (属性动画 / Property Animation) (Android 3.0+)


###Animation 与 Animator 对比


- 原理：Animation通过改变View的Matrix，不断重绘实现动画，Animator则直接调用Object的setter方法
- 前者只改变界面显示，即使View的显示位置发生变化，点击事件还是发生在原来的地方
- 前者只能作用于View，后者可以作用于任意Object
- 前者只能改变View的显示效果，后者可以平滑改变Object的任意属性


- 前者的执行效率相对较高，后者需要使用反射，效率较低
- 后者在Android 3.0+版本中才能使用


###相关的类和继承关系

- 定义了新的TimeInterpolator插值器，为了兼容性，原有的Interpolator继承自TimeInterpolator。




##ValueAnimator

ValueAnimator使用示例

```
ValueAnimator anim = ValueAnimator.ofInt(1, 100);
anim.setDuration(3000);

anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
   @Override
   public void onAnimationUpdate(ValueAnimator animation) {
       Integer val = (Integer) animation.getAnimatedValue();
       mTextView.setText(String.valueOf(val));
   }
});

anim.addListener(new Animator.AnimatorListener() {
   @Override
   public void onAnimationStart(Animator animation) {
   }

   @Override
   public void onAnimationEnd(Animator animation) {
       mTextView.setText("HelloWorld");
   }

   @Override
   public void onAnimationCancel(Animator animation) {
   }

   @Override
   public void onAnimationRepeat(Animator animation) {
   }
});

// API Level 19 (Android 4.4) 才可以使用
anim.addPauseListener(new Animator.AnimatorPauseListener() {
   @Override
   public void onAnimationPause(Animator animation) {
   }

   @Override
   public void onAnimationResume(Animator animation) {
   }
});

anim.start();
```

1. ValueAnimator有四个静态方法可用于实例化：
```
public static ValueAnimator ofInt(int... values);
public static ValueAnimator ofFloat(float... values);
public static ValueAnimator ofPropertyValuesHolder(PropertyValuesHolder... values);
public static ValueAnimator ofObject(TypeEvaluator evaluator, Object... values);
```
  - 其中前两个方法可以根据设定的起始值和终止值生成int、float序列。
  - 第三个方法用于从`PropertyValuesHolder`获取数据生成动画，后面介绍。
  - `ofObject`方法配合自定义的`TypeEvaluator`，可以计算任意类型的数据序列。
2. AnimatorListener, AnimatorPauseListener, AnimatorUpdateListener

##TypeEvaluator


```
PointF start = new PointF(0, 0);
PointF end = new PointF(mTextView.getX(), mTextView.getY());

ValueAnimator anim = ValueAnimator.ofObject(new MyTypeEvaluator(), start, end);
anim.setDuration(2000);
anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
   @Override
   public void onAnimationUpdate(ValueAnimator animation) {
       PointF val = (PointF) animation.getAnimatedValue();
       mTextView.setX(val.x);
       mTextView.setY(val.y);
   }
});
anim.start();
```

```
class MyTypeEvaluator implements TypeEvaluator<PointF> {

   /**
    * @param fraction   当前帧 0 ~ 1f
    * @param startValue 起始值
    * @param endValue   终止值
    * @return 当前帧的value插值计算结果
    */
   @Override
   public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
       return new PointF(
               startValue.x + fraction * (endValue.x - startValue.x),
               startValue.y + fraction * (endValue.y - startValue.y)
       );
   }
}
```

##ObjectAnimator

用法示例

```
Animator[] anim = new Animator[3];
anim[0] = ObjectAnimator.ofFloat(mTextView, "alpha", 0, 1);
anim[1] = ObjectAnimator.ofFloat(mTextView, "scaleX", 2f, 1f);
anim[2] = ObjectAnimator.ofFloat(mTextView, "translationY", -300, 0);

for (Animator a : anim) {
   a.setDuration(1000);
}

AnimatorSet set = new AnimatorSet();
set.playTogether(anim);
set.start();
```

1. 方法ofFloat中的参数：
  - 第一个为Target，即要对哪个对象执行动画；
  - 第二个为属性，这里用的是字符串，执行动画时会用反射，自动调用字符串对应属性的setter；
  - 后面的参数，用于设置属性的值。
2. `AnimatorSet.playTogether` 方法，指定并行执行每个Animator动画。因为AnimatorSet继承自Animator，所以该方法的参数也可以为其他`AnimatorSet`。

###AnimatorSet.playSequentially

```
Animator[] anim = new Animator[4];
anim[0] = ObjectAnimator.ofFloat(mTextView, "translationX", 0, 100);
anim[1] = ObjectAnimator.ofFloat(mTextView, "translationY", 0, 100);
anim[2] = ObjectAnimator.ofFloat(mTextView, "translationX", 100, 0);
anim[3] = ObjectAnimator.ofFloat(mTextView, "translationY", 100, 0);

for (Animator a : anim) {
   a.setDuration(600);
}

AnimatorSet set = new AnimatorSet();
set.playSequentially(anim);
set.start();
```

- `AnimatorSet.playSequentially`方法，指定依次执行每个Animator动画。

###AnimatorSet.Builder

```
Animator[] anim = new Animator[4];
anim[0] = ObjectAnimator.ofFloat(mTextView, "translationX", 0, 100);
anim[1] = ObjectAnimator.ofFloat(mTextView, "translationY", 0, 100);
anim[2] = ObjectAnimator.ofFloat(mTextView, "translationX", 100, 0);
anim[3] = ObjectAnimator.ofFloat(mTextView, "translationY", 100, 0);

for (Animator a : anim) {
   a.setDuration(600);
}

AnimatorSet set1 = new AnimatorSet();
AnimatorSet set2 = new AnimatorSet();

set1.play(anim[0]).before(anim[1]);
set1.play(anim[2]).after(anim[1]);

set2.play(anim[3]).after(set1);
set2.start();
```

1. AnimatorSet.play方法，返回一个AnimatorSet.Builder实例，可以调用Builder的before、after、with，指定与其他动画之间的播放次序。
2. AnimatorSet的播放可以嵌套。
3. **注意：** 
  - AnimatorSet.play方法返回的是一个新的Builder实例
  - 而Builder的before、after、with方法返回的是这个Builder实例自身，要慎用Builder的连写方式。
  - 下面的代码表示anim[0]播放完后，同时播放anim[1]和anim[2]，而不是依次播放三个动画。详见AnimatorSet.Builder的注释说明。
  `set.play(anim[0]).before(anim[1]).before(anim[2]);`


##PropertyValuesHolder

```
PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("x", 0, mTextView.getX());
PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("y", 0, mTextView.getY());
PropertyValuesHolder pvhA = PropertyValuesHolder.ofFloat("alpha", 0, 1);

ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(mTextView, pvhX, pvhY, pvhA);
anim.setDuration(1000);
anim.start();
```

- 每个PropertyValuesHolder包含一个Property和对应的Value
- 一个ObjectAnimator可以包含多个PropertyValuesHolder
- 使用PropertyValuesHolder可以在一个Animator中同时改变Target的多个属性。实现同样的效果也可以使用多个Animator并行执行，但效率相对较低。


##KeyFrame

```
final float y = mTextView.getY();
// fraction, value
Keyframe kf[] = new Keyframe[]{
       Keyframe.ofFloat(0f, 0),
       Keyframe.ofFloat(0.2f, 0.4f * y),
       Keyframe.ofFloat(0.5f, 0.3f * y),
       Keyframe.ofFloat(0.8f, 0.8f * y),
       Keyframe.ofFloat(1f, y)
};

PropertyValuesHolder pvhK = PropertyValuesHolder.ofKeyframe("y", kf);

ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(mTextView, pvhK);
anim.setDuration(5000);
anim.start();
```

- 不使用KeyFrame时，随着fraction的均匀递增，value的值线性增长。配合LinearInterpolator可以实现View匀速运动。
- 使用KeyFrame后，每两个关键帧之间成为一段动画，期间Value值线性变化。右图中每个点表示一个KeyFrame。
- 使用KeyFrame时，也可以使用Interpolator。
- **注意：**使用KeyFrame时，随着时间推移，Value的值不仅可以递增，也可以递减。
- **注意：**即使使用的是LinearInterpolator，最后Value的值改变并不一定是均匀的，因为每两个KeyFrame之间连线的斜率不一样。



##CustomTarget

```
Object target = new CustomTarget(mTextView);
ObjectAnimator anim = ObjectAnimator.ofFloat(target, "translation", 0, 1);
anim.setDuration(2000);
anim.start();
```

```
class CustomTarget {
   private View mView;
   private final float x;
   private final float y;

   public CustomTarget(View view) {
       mView = view;
       x = view.getX();
       y = view.getY();
   }

   public void setTranslation(float translation) {
       mView.setX(translation * x);
       mView.setY(translation * y);
   }
}
```


##CustomProperty

代码示例：给TextView定义一个IntegerAlpha的属性，其取值为0~256的整型值。

```
PropertyValuesHolder pvh = PropertyValuesHolder.ofInt(new CustomProperty(Integer.class, "integerAlpha"), 0, 256);

ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(mTextView, pvh);
anim.setDuration(2000);
anim.start();
```

```
class CustomProperty extends Property<TextView, Integer> {

   public CustomProperty(Class<Integer> type, String name) {
       super(type, name);
   }

   @Override
   public void set(TextView tv, Integer alpha) {
       tv.setAlpha((float) alpha / 256);
   }

   @Override
   public Integer get(TextView tv) {
       return (int) (tv.getAlpha() * 256);
   }
}
```


##ViewPropertyAnimator

ViewPropertyAnimator提供了一种快速便捷的方式，可以直接生成View的动画，多个属性动画同时执行。

```
float x = mTextView.getX();
float y = mTextView.getY();
mTextView.setX(0);
mTextView.setY(0);
mTextView.setAlpha(0);

ViewPropertyAnimator anim = mTextView.animate();
anim.x(x);
anim.y(y);
anim.alpha(1);
anim.setDuration(3000);
anim.setInterpolator(new BounceInterpolator());
anim.start();
```

##LayoutTransition

```
Object o = null;
Animator animIn = ObjectAnimator.ofPropertyValuesHolder(
       o,
       PropertyValuesHolder.ofFloat("translationX", 200, 0),
       PropertyValuesHolder.ofFloat("alpha", 0, 1)
);
Animator animOut = ObjectAnimator.ofPropertyValuesHolder(
       o,
       PropertyValuesHolder.ofFloat("translationX", 0, 200),
       PropertyValuesHolder.ofFloat("alpha", 1, 0)
);
animIn.setDuration(500);
animOut.setDuration(500);

LayoutTransition lt = new LayoutTransition();
mContainer.setLayoutTransition(lt);

lt.setAnimator(LayoutTransition.APPEARING, animIn);
lt.setAnimator(LayoutTransition.DISAPPEARING, animOut);

Button bn = new Button(this);
bn.setText("-");
bn.setOnClickListener(new View.OnClickListener() {
   @Override
   public void onClick(View v) {
       mContainer.removeView(v);
   }
});
mContainer.addView(bn, index);
```

1. LayoutTransition可以设置ViewGroup中布局发生变化时，ChildView的动画
2. transitionType
 - APPEARING: View出现
 - DISAPPEARING: View消失
 - CHANGE_APPEARING: 由于其他View出现而需要改变位置
 - CHANGE_DISAPPEARING: 由于其他View消失而需要改变位置
 - CHANGING: 由于Layout改变而需要改变位置
3. 可以给每个TranslationType设置一个Animator，发生变化时，对应的ChildView就会执行相应的动画。
4. 可以在XML中开启ViewGroup的LayoutTransition属性，布局发生变化时，会有一套默认的动画被执行。
5. **注意一个比较特别的问题：**
  这里执行动画的Target，是布局发生改变的ChildView，因此不需要在Animator中指定Target。但是实际试验发现：
  - 需要用ObjectAnimator实例，并且Target设置为null或者任意对象，动画才能正常执行；
  - 而如果用没有Target的ValueAnimator，动画不能正常播放。

**特殊案例：**
- `ValueAnimator`中有一个常用来获取实例的静态方法`public static ValueAnimator ofPropertyValuesHolder(PropertyValuesHolder... values);`

- 而`ObjectAnimator`继承自`ValueAnimator`，继承了上面的静态方法。同时`ObjectAnimator`中又有一个同名不同参数的静态方法`public static ObjectAnimator ofPropertyValuesHolder(Object target, PropertyValuesHolder... values);`

- 下面代码的原意，是想把ObjectAnimator的Target设置为null，同时添加两个PropertyValuesHolder。但是实际上这个参数匹配的是`ValueAnimator`中的方法，导致动画不能执行。
```
Animator animIn = ObjectAnimator.ofPropertyValuesHolder(
       null,
       PropertyValuesHolder.ofFloat("translationX", 200, 0),
       PropertyValuesHolder.ofFloat("alpha", 0, 1)
);
```
- 为了让代码正确执行，可以这么写
```
Object o = null;
Animator animIn = ObjectAnimator.ofPropertyValuesHolder(
       o,
       PropertyValuesHolder.ofFloat("translationX", 200, 0),
       PropertyValuesHolder.ofFloat("alpha", 0, 1)
);
```


##Dialog View Animation

给Dialog中的View设置动画

```
Animation anim = AnimationUtils.loadAnimation(this, R.anim.animation);

View dialogLayout = LayoutInflater.from(this).inflate(R.layout.dialog, null);

View v = dialogLayout.findViewById(R.id.dialog_text);
v.startAnimation(anim);

new AlertDialog.Builder(this).setView(dialogLayout).show();
```

##Dialog Animation

给整个Dialog设置动画（即Window对象）

```
<style name="dialogAnimation" parent="@android:style/Animation.Dialog">
   <item name="android:windowEnterAnimation">@anim/anim_in</item>
   <item name="android:windowExitAnimation">@anim/anim_out</item>
</style>
```

```
Dialog dialog = new AlertDialog.Builder(this)
       .setMessage("Message")
       .setPositiveButton("OK", null)
       .create();
dialog.getWindow().setWindowAnimations(R.style.dialogAnimation);
dialog.show();
```

##PopupWindow Animation

XML中的定义和Dialog相同

```
<style name="dialogAnimation" parent="@android:style/Animation.Dialog">
   <item name="android:windowEnterAnimation">@anim/anim_in</item>
   <item name="android:windowExitAnimation">@anim/anim_out</item>
</style>
```

Java代码中设置动画

```
mPopupWindow.setAnimationStyle(R.style.dialogAnimation);
```

##Activity Animation From XML

```
<!-- 1.定义Activity动画 -->
<style name="activityAnimation" parent="@android:style/Animation.Activity">

   <item name="android:windowEnterAnimation">@null</item>
   <item name="android:windowExitAnimation">@null</item>

   <!-- 新Activity启动时，Enter动画 -->
   <item name="android:activityOpenEnterAnimation">@anim/anim_in</item>
   <!-- 新Activity启动时，原有Activity的Exit动画 -->
   <item name="android:activityOpenExitAnimation">@anim/stay</item>

   <!-- 新Activity退出时，原有Activity的Enter动画 -->
   <item name="android:activityCloseEnterAnimation">@anim/stay</item>
   <!-- 新Activity退出时，Exit动画 -->
   <item name="android:activityCloseExitAnimation">@anim/anim_out</item>

</style>
```

```
<!-- 2. AppTheme中引用Activity动画 -->
<style name="AppTheme" parent="android:Theme.Holo.Light.NoActionBar">
   <item name="android:windowAnimationStyle">@style/activityAnimation</item>
</style>
```

```
<!-- 3. Manifest中的Application或Activity标签中，指定theme为AppTheme -->
<application
   android:theme="@style/AppTheme">
```

**注意：**
- 如果Activity的launchMode设置为SingleInstance，可能会导致动画失效
- 部分手机需要在设置中开启显示全部动画。
- 某些手机由于定制了ROM，通过XML自定义Activity切换动画无效，只能用Java实现。

##Activity Animation From Java

```
startActivity(new Intent(this, NewActivity.class));
overridePendingTransition(R.anim.anim_in, R.anim.anim_out); // 设置为0则表示没有动画
```
