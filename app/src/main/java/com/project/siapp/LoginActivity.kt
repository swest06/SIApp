package com.project.siapp

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.nfc.Tag
import android.os.Bundle
import android.os.PersistableBundle
//import android.support.constraint.ConstraintLayout
//import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class LoginActivity: AppCompatActivity() {

    private val TAG = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Animated gradient background
        animateBackground()
//        val constraintLayout: ConstraintLayout = findViewById(R.id.layout)
//        val animationDrawable: AnimationDrawable = constraintLayout.background as AnimationDrawable
//        animationDrawable.setEnterFadeDuration(2000)
//        animationDrawable.setExitFadeDuration(4000)
//        animationDrawable.start()

        //

        //Register page
        val registerLink = findViewById<TextView>(R.id.register_link)
        registerLink.setOnClickListener {
            Log.d(TAG, "'Don't have an account?' clicked")

            //launch login activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
    fun animateBackground(){
        val constraintLayout: ConstraintLayout = findViewById(R.id.layout)
        val animationDrawable: AnimationDrawable = constraintLayout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(2000)
        animationDrawable.setExitFadeDuration(4000)
        animationDrawable.start()
    }
}