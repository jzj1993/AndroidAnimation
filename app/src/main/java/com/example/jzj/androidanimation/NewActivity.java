package com.example.jzj.androidanimation;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

/**
 * Created by jzj on 15/8/7.
 */
public class NewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = new View(this);
        v.setBackgroundColor(Color.rgb(200, 240, 230));
        setContentView(v);
    }
}
