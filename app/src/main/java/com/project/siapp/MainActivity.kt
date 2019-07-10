package com.project.siapp

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
//import android.support.v7.app.AppCompatActivity
import android.os.Bundle
//import android.support.constraint.ConstraintLayout
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        //Animated gradient background
        animateBackground()

        //Assign views to constants
        val register = findViewById<Button>(R.id.register_button)
        val loginLink = findViewById<TextView>(R.id.login_link)

        //Register new user
        register.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                registerUser()
            }
        })

        //Link to login screen
        loginLink.setOnClickListener {
            Log.d(TAG, "'Already have and account?' clicked")

            //launch login activity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    public fun animateBackground(){
        val constraintLayout: ConstraintLayout = findViewById(R.id.layout)
        val animationDrawable: AnimationDrawable = constraintLayout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(2000)
        animationDrawable.setExitFadeDuration(4000)
        animationDrawable.start()
    }

    private fun registerUser(){
        val email = (findViewById<EditText>(R.id.email_editText)).text.toString()
        val password = (findViewById<EditText>(R.id.password_editText)).text.toString()

        //Check email and password has values
        if  (email.isEmpty()){
            Toast.makeText(this@MainActivity, "please enter a valid email", Toast.LENGTH_SHORT).show()
//            return@onClick
            return@registerUser

        } else if (password.isEmpty()) {
            Toast.makeText(this@MainActivity, "Please enter a valid password", Toast.LENGTH_SHORT).show()
//            return@onClick
            return@registerUser

        } else if(password.length < 6){
            Toast.makeText(this@MainActivity, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show()
//            return@onClick
            return@registerUser
        }

        //test login with Logs
        Log.d(TAG, "Email is: $email")
        Log.d(TAG, "Password is: $password")

        //Firebase Authentication for new user
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                Log.d(TAG, "Inside 'createUserWithEmailAndPassword(email, password)'")
                if (!it.isSuccessful) {
                    Log.d(TAG, "Unsuccessful")
                    return@addOnCompleteListener
                } else {
                    Log.d(TAG, "Successful")
                    Log.d(TAG, "New user created \n User ID: ${it.result?.user?.uid}")
                }
            }
            .addOnFailureListener{
                Log.d(TAG, "Register failure \n user: ${it.message}")
            }
    }
}
