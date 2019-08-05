package com.project.siapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_other_user_profile.*


class OtherUserProfileActivity : AppCompatActivity() {
    private val messageButton by lazy { findViewById<Button>(R.id.message_button_other_user_profile) }
    private val addToGroupButton by lazy { findViewById<Button>(R.id.add_to_group_button_other_user_profile) }

    //parcel object
    val user by lazy { intent.getParcelableExtra<User>(SearchActivity.USER_KEY) }

    //string values for user
    val username by lazy { intent.getStringExtra(SearchActivity.USER_NAME) }
    val userLocation by lazy { intent.getStringExtra(SearchActivity.USER_LOCATION) }
    val userGender by lazy { intent.getStringExtra(SearchActivity.USER_GENDER) }
    val userAge by lazy { intent.getStringExtra(SearchActivity.USER_AGE) }
    val userId by lazy { intent.getStringExtra(SearchActivity.USER_ID) }
    val userPhoto by lazy { intent.getStringExtra(SearchActivity.USER_PHOTO) }
    val userAbout by lazy { intent.getStringExtra(SearchActivity.USER_ABOUT) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_user_profile)
        supportActionBar?.title = "Profile $username"

        //set profile info
        setFields()

        //Start chat log
        messageButton.setOnClickListener {
            startChatLog()
        }
    }

    fun startChatLog(){
        val intent = Intent(this, ChatLogActivity::class.java)

        //Needs fixing!!
        intent.putExtra("USER_NAME", username)
        intent.putExtra("USER_ID", userId)
        intent.putExtra("USER_PHOTO", userPhoto)

        startActivity(intent)
    }

    fun setFields(){
        name_textView_other_user_profile.setText(username)
        location_textView_other_user_profile.setText(userLocation)
        age_textView_other_user_profile.setText(userAge)
        gender_textView_other_user_profile.setText(userGender)
        aboutInfo_textView_other_user_profile.setText(userAbout)
    }
}
