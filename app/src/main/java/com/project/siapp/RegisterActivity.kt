package com.project.siapp

import android.content.Intent
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
import com.google.firebase.database.FirebaseDatabase


class RegisterActivity : AppCompatActivity() {
    private val TAG = "RegisterActivity"
    private val register by lazy { findViewById<Button>(R.id.register_button) }
    private val loginLink by lazy { findViewById<TextView>(R.id.login_link) }
    private val constraintLayout by lazy { findViewById<ConstraintLayout>(R.id.layout) }
    private lateinit var name: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var toast: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Animated gradient background
        animateBackground(constraintLayout)

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

    /**
     * Creates and authenticates new user when register button is clicked
     */
    private fun registerUser(){
        name = (findViewById<EditText>(R.id.name_editText_register)).text.toString()
        email = (findViewById<EditText>(R.id.email_editText)).text.toString()
        password = (findViewById<EditText>(R.id.password_editText)).text.toString()
        toast = getToast(this@RegisterActivity)

        //Check name, email and password has values
        if (name.isEmpty()) {
            toast.setText("What is your name?")
            toast.show()
            return

        }else if  (email.isEmpty()){
            toast.setText("please enter a valid email")
            toast.show()
            return@registerUser

        } else if (password.isEmpty()) {
            toast.setText( "Please enter a valid password")
            toast.show()
            return@registerUser

        } else if(password.length < 6){
            toast.setText("Password should be at least 6 characters long")
            toast.show()
            return@registerUser
        }

        //test login with Logs
        Log.d(TAG, "Email is: $email")
        Log.d(TAG, "Password is: $password")
        toast.setText("Register unsuccessful. \n" +
                "Please try again or restart app")

        //Firebase Authentication for new user
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                Log.d(TAG, "Inside 'createUserWithEmailAndPassword(email, password)'")
                if (!it.isSuccessful) {
                    Log.d(TAG, "Unsuccessful")
                    toast.show()
                    return@addOnCompleteListener
                } else {
                    Log.d(TAG, "Successful")
                    Log.d(TAG, "New user created \n User ID: ${it.result?.user?.uid}")

                    //Save user to Firebase db
                    saveUser()
                }
            }
            .addOnFailureListener{
                Log.d(TAG, "Register failure \n user: ${it.message}")
                toast.show()
                return@addOnFailureListener
            }
    }

    private fun saveUser(){
        //create db entry with user id
        val uid = FirebaseAuth.getInstance().uid?:""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        //store user as object and save to db
        val user = User(uid, name)
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d(TAG, "User was saved to Firebase")

                //Go to user's profile page
                val intent = Intent(this, ProfilePageActivity::class.java)

                //Clear other activites from the stack
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to save user to Firebase")
            }
    }

}
