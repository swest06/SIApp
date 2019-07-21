package com.project.siapp

import android.content.Intent
import android.os.Bundle
//import android.support.constraint.ConstraintLayout
//import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.auth.FirebaseAuth

class LoginActivity: AppCompatActivity() {

    private val TAG = "LoginActivity"
    private val constraintLayout by lazy { findViewById<ConstraintLayout>(R.id.layout) }
    private val login by lazy {findViewById<Button>(R.id.login_button)}
    private val registerLink by lazy { findViewById<TextView>(R.id.register_link) }
    private lateinit var email: String
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
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Tries to login user when they click login button
     */
    private fun loginUser(){
        email = (findViewById<EditText>(R.id.name_editText_register)).text.toString()
        password = (findViewById<EditText>(R.id.password_editText_login)).text.toString()
        toast = getToast(this@LoginActivity)

        //Check username and password strings
        if (email.isEmpty()){
            Log.d(TAG, "inside email.isEmpty")
            toast.setText("Please enter a username")
            toast.show()
            return

        } else if (password.isEmpty()){
            toast.setText("Please enter a password")
            toast.show()
            return
        }

        //Login User
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful){
                    Log.d(TAG, "Unsuccessful login")
                    return@addOnCompleteListener
                } else{
                    Log.d(TAG, "Successful login")
                    Log.d(TAG, "User ID: ${it.result?.user?.uid}")

                    //Go to user's profile page
                    val intent = Intent(this, ProfilePageActivity::class.java)
                    startActivity(intent)
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "Login failure")
            }
    }
}