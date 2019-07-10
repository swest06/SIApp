package com.project.siapp

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
//import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.lang.Exception

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

        val background = object : Thread(){
            override fun run() {
                try{
                    Thread.sleep(5000)

                    val intent = Intent(baseContext, MainActivity::class.java)
//                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }
        background.start()
    }
}