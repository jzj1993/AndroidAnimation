package com.example.jzj.androidanimation;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Property;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MainActivity extends Activity {

    /**
     * 用于容纳按钮的LinearLayout
     */
    private LinearLayout mContainer;
    /**
     * 用于展示动画的TextView
     */
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    public void onBackPressed() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
            mPopupWindow = null;
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
            mPopupWindow = null;
        }
    }

    void addButton(String text, View.OnClickListener listener) {
        Button button = new Button(this);
        button.setText(text);
        button.setOnClickListener(listener);
        button.setTextSize(10);
        mContainer.addView(button);
    }

    private void init() {
        mContainer = (LinearLayout) findViewById(R.id.buttons_container);
        mTextView = (TextView) findViewById(R.id.text_view);
        addButton("Frame Animation", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameAnimation();
            }
        });
        addButton("Animation From XML", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationFromXml();
            }
        });
        addButton("AnimationSet From XML", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationSetFromXml();
            }
        });
        addButton("AnimationSet From XML Scale Coordinate", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationSetFromXmlScaleCoordinate();
            }
        });
        addButton("Animation From Java", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationFromJava();
            }
        });
        addButton("Custom Animation and Interpolator", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customAnimation_CustomInterpolator_Listener();
            }
        });
        addButton("Value Animator", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valueAnimator();
            }
        });
        addButton("TypeEvaluator", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeEvaluator();
            }
        });
        addButton("AnimatorSet, ObjectAnimator", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animatorSet_ObjectAnimator();
            }
        });
        addButton("AnimatorSet PlaySequentially", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animatorSetPlaySequentially();
            }
        });
        addButton("AnimatorSet.Builder", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animatorSet_Builder();
            }
        });
        addButton("Animator From XML", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animatorFromXml();
            }
        });
        addButton("PropertyValuesHolder", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                propertyValuesHolder();
            }
        });
        addButton("KeyFrame", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyFrame();
            }
        });
        addButton("Custom Target", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customTarget();
            }
        });
        addButton("Custom Property", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customProperty();
            }
        });
        addButton("ViewPropertyAnimator", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPropertyAnimator();
            }
        });
        addButton("LayoutTransition", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutTransition(mContainer.indexOfChild(v));
            }
        });
        addButton("Dialog View Animation", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogViewAnimation();
            }
        });
        addButton("Dialog Animation", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAnimation();
            }
        });
        addButton("PopupWindow Animation", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindowAnimation();
            }
        });
        addButton("Activity Animation From XML", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityAnimationFromXml();
            }
        });
        addButton("Activity Animation From Java", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityAnimationFromJava();
            }
        });
    }

    private AnimationDrawable mFrameAnimation = null;

    /**
     * 逐帧动画
     */
    void frameAnimation() {
        if (mFrameAnimation == null) {
            mFrameAnimation = (AnimationDrawable) getResources().getDrawable(R.drawable.frame_anim); // 从XML加载动画
            mTextView.setBackground(mFrameAnimation);
            mFrameAnimation.start();
        } else {
            mFrameAnimation.stop();
            mFrameAnimation = null;
            mTextView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    /**
     * Animation：从XML加载
     */
    void animationFromXml() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.animation);
        mTextView.startAnimation(anim);
    }

    /**
     * 从XML加载一组动画
     */
    void animationSetFromXml() {
        // 返回的是AnimationSet实例，AnimationSet继承自Animation
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.animation_set);
        mTextView.startAnimation(anim);
    }

    /**
     * 从XML加载一组动画
     */
    void animationSetFromXmlScaleCoordinate() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.animation_set_scale_coord);
        mTextView.startAnimation(anim);
    }

    /**
     * Java生成动画
     */
    void animationFromJava() {
        Animation anim = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -0.2f, Animation.RELATIVE_TO_PARENT, -0.2f,
                Animation.RELATIVE_TO_PARENT, -0.5f, Animation.RELATIVE_TO_PARENT, 0
        );
        anim.setFillAfter(true);
        anim.setDuration(2000);
        mTextView.setAnimation(anim);
    }

    /**
     * 自定义Animation
     * <p/>
     * 自定义Interpolator
     * <p/>
     * AnimationListener
     */
    void customAnimation_CustomInterpolator_Listener() {
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
    }

    /**
     * ValueAnimator
     * <p/>
     * AnimatorListener
     * <p/>
     * AnimatorUpdateListener
     */
    void valueAnimator() {
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
//        API Level 19
//        anim.addPauseListener(new Animator.AnimatorPauseListener() {
//            @Override
//            public void onAnimationPause(Animator animation) {
//            }
//
//            @Override
//            public void onAnimationResume(Animator animation) {
//            }
//        });
        anim.start();
    }

    /**
     * TypeEvaluator
     */
    void typeEvaluator() {
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
    }

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

    /**
     * ObjectAnimator: translate, scale, alpha
     * <p/>
     * Animator.playTogether
     */
    void animatorSet_ObjectAnimator() {

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
    }

    /**
     * Animator.playSequentially
     */
    void animatorSetPlaySequentially() {

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
    }

    /**
     * AnimatorSet.Builder
     */
    void animatorSet_Builder() {
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

        // 慎用Builder的连写方式：下面的代码表示anim[0]播放完后，同时播放anim[1]和anim[2]，而不是依次播放三个动画。
        // 详见AnimatorSet.Builder的注释说明。
        // set.play(anim[0]).before(anim[1]).before(anim[2]);
    }

    /**
     * Animator from XML
     */
    void animatorFromXml() {
        AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.animator);
        set.setTarget(mTextView);
        set.start();
    }

    /**
     * PropertyValuesHolder
     */
    void propertyValuesHolder() {
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("x", 0, mTextView.getX());
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("y", 0, mTextView.getY());
        PropertyValuesHolder pvhA = PropertyValuesHolder.ofFloat("alpha", 0, 1);

        ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(mTextView, pvhX, pvhY, pvhA);
        anim.setDuration(1000);
        anim.start();
    }

    void keyFrame() {
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
        anim.setInterpolator(new LinearInterpolator());
        anim.start();
    }

    /**
     * 自定义Target
     */
    void customTarget() {
        Object target = new CustomTarget(mTextView);
        ObjectAnimator anim = ObjectAnimator.ofFloat(target, "translation", 0, 1);
        anim.setDuration(2000);
        anim.start();
    }

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

    /**
     * 自定义Property
     */
    void customProperty() {
        PropertyValuesHolder pvh = PropertyValuesHolder.ofInt(new CustomProperty(Integer.class, "integerAlpha"), 0, 256);

        ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(mTextView, pvh);
        anim.setDuration(2000);
        anim.start();
    }

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

    /**
     * ViewPropertyAnimator
     */
    void viewPropertyAnimator() {
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
    }

    /**
     * LayoutTransition
     *
     * @param index
     */
    void layoutTransition(int index) {
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
    }

    /**
     * 给Dialog中的View设置动画
     */
    void dialogViewAnimation() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.animation);

        View dialogLayout = LayoutInflater.from(this).inflate(R.layout.dialog, null);

        View v = dialogLayout.findViewById(R.id.dialog_text);
        v.startAnimation(anim);

        new AlertDialog.Builder(this).setView(dialogLayout).show();
    }

    /**
     * 给整个Dialog设置动画（即Window对象）
     */
    void dialogAnimation() {
        Dialog dialog = new AlertDialog.Builder(this)
                .setMessage("Message")
                .setPositiveButton("OK", null)
                .create();
        dialog.getWindow().setWindowAnimations(R.style.dialogAnimation);
        dialog.show();
    }

    private PopupWindow mPopupWindow;

    /**
     * PopupWindow动画
     */
    void popupWindowAnimation() {
        if (mPopupWindow == null) {
            View v = LayoutInflater.from(this).inflate(R.layout.dialog, null);
            mPopupWindow = new PopupWindow(
                    v,
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(0));
            mPopupWindow.setAnimationStyle(R.style.dialogAnimation);
            mPopupWindow.showAtLocation(mContainer, Gravity.CENTER, 0, 0);
        } else {
            mPopupWindow.dismiss();
            mPopupWindow = null;
        }
    }

    /**
     * 启动新的Activity时动画效果
     */
    void activityAnimationFromXml() {
        startActivity(new Intent(this, NewActivity.class));
    }

    /**
     * 启动新的Activity时动画效果，使用Java代码直接实现
     */
    void activityAnimationFromJava() {
        startActivity(new Intent(this, NewActivity.class));
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out); // 设置为0则表示没有动画
    }
}
