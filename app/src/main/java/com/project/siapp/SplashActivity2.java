package com.project.siapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import org.jetbrains.annotations.Nullable;

public class SplashActivity2 extends AppCompatActivity {

    private Handler mHandler;
    private Runnable mRunnable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //First method (worked but kept fading in/out splash image on main activity screen)
//        mRunnable = new Runnable() {
//            @Override
//            public void run() {
//                startActivity(new Intent(getApplicationContext(), MainActivity.class));
//            }
//        };
//
//        mHandler = new Handler();
//
//        mHandler.postDelayed(mRunnable, 3000);

        //Second method (app wouldn't even start. Comment on youtube video)
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(SplashActivity2.this, MainActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        }, 3000);
    }

//    @Override
//    protected void onDestroy(){
//        super.onDestroy();
//        if (mHandler != null && mRunnable != null)
//        mHandler.removeCallbacks(mRunnable);
//    }
}
