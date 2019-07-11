package com.project.siapp

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.nfc.Tag
import android.os.Bundle
import android.os.PersistableBundle
//import android.support.constraint.ConstraintLayout
//import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.project.siapp.MainActivity
import com.project.siapp.animateBackground

class LoginActivity: AppCompatActivity() {

    private val TAG = "LoginActivity"
    private val constraintLayout by lazy { findViewById<ConstraintLayout>(R.id.layout) }
    private val login by lazy {findViewById<Button>(R.id.login_button)}
    private val registerLink by lazy { findViewById<TextView>(R.id.register_link) }
    private lateinit var userName: String
    private lateinit var password: String
    private lateinit var toast: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Animated gradient background
        animateBackground(constraintLayout)

        //User login
        login.setOnClickListener {
            loginUser()
        }


        //Register page
        registerLink.setOnClickListener {
            Log.d(TAG, "'Don't have an account?' clicked")

            //launch login activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Tries to login user when they click login button
     */
    private fun loginUser(){
        userName = findViewById<EditText>(R.id.username_editText_login).toString()
        password = findViewById<EditText>(R.id.password_editText).toString()
        toast = getToast(this@LoginActivity)

        if (userName.isEmpty()){
            toast.setText("Please enter a username")
            toast.show()
            return@loginUser

        } else if (password.isEmpty()){
            toast.setText("Please enter a password")
            toast.show()
            return@loginUser
        }

    }
}