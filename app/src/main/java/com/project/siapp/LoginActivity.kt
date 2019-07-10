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
import com.project.siapp.MainActivity
import com.project.siapp.animateBackground

class LoginActivity: AppCompatActivity() {

    private val TAG = "LoginActivity"
    private val constraintLayout by lazy { findViewById<ConstraintLayout>(R.id.layout) }
    private val registerLink by lazy { findViewById<TextView>(R.id.register_link) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Animated gradient background
        animateBackground(constraintLayout)

        //Register page
        registerLink.setOnClickListener {
            Log.d(TAG, "'Don't have an account?' clicked")

            //launch login activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}