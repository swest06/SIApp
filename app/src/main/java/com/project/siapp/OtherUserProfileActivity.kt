package com.project.siapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_other_user_profile.*


class OtherUserProfileActivity : AppCompatActivity() {
    private val messageButton by lazy { findViewById<Button>(R.id.message_button_other_user_profile) }
    private val addToGroupButton by lazy { findViewById<Button>(R.id.add_to_group_button_other_user_profile) }
    private val TAG = "OtherUserProfileActiv"

    //parcel object
    val user by lazy { intent.getParcelableExtra<User>(SearchActivity.USER_KEY) }

    //string values for user (Can delete if parcelized user object works correctly)
    val username by lazy { intent.getStringExtra(SearchActivity.USER_NAME) }
    val userLocation by lazy { intent.getStringExtra(SearchActivity.USER_LOCATION) }
    val userGender by lazy { intent.getStringExtra(SearchActivity.USER_GENDER) }
    val userAge by lazy { intent.getStringExtra(SearchActivity.USER_AGE) }
    val userId by lazy { intent.getStringExtra(SearchActivity.USER_ID) }
    val userPhoto by lazy { intent.getStringExtra(SearchActivity.USER_PHOTO) }
    val userAbout by lazy { intent.getStringExtra(SearchActivity.USER_ABOUT) }

    val photoButton by lazy { findViewById<Button>(R.id.photo_other_user_profile) }
    val groupButton by lazy { findViewById<Button>(R.id.add_to_group_button_other_user_profile) }

    //dummy text for testing.
    val dummyText: String = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor" +
            "incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation\n" +
            "    ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit\n" +
            "    in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat\n" +
            "    non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."




    //constants to pass to intents (may just end up using user id)
    companion object{
        val USER_KEY = "USER_KEY"

        val USER_ID = "USER_ID"
        val USER_NAME = "USER_NAME"
        val USER_AGE = "USER_AGE"
        val USER_LOCATION = "USER_LOCATION"
        val USER_GENDER = "USER_GENDER"
        val USER_ABOUT = "USER_ABOUT"
        val USER_PHOTO = "USER_PHOTO"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_user_profile)
        supportActionBar?.title = "Profile $username"
        photoButton.alpha = 0F

        //set profile info
        setFields()

        //Start chat log
        messageButton.setOnClickListener {
            startChatLog()
        }

        groupButton.setOnClickListener {
            val intent = Intent(this, GroupActivity::class.java)
            Log.d(TAG, "Inside group button click listener")
            startActivity(intent)
        }
    }

    fun startChatLog(){
        val intent = Intent(this, ChatLogActivity::class.java)

        //Needs fixing!!
        intent.putExtra(USER_NAME, username)
        intent.putExtra(USER_ID, userId)
        intent.putExtra(USER_PHOTO, userPhoto)
        intent.putExtra(USER_KEY, user)

        startActivity(intent)
    }

    fun setFields(){
        name_textView_other_user_profile.setText(username)
        location_textView_other_user_profile.setText(userLocation)
        age_textView_other_user_profile.setText(userAge)
        gender_textView_other_user_profile.setText(userGender)

        //Only for testing purposes. Delete for production
        if (!userAbout.isEmpty() || null != userAbout){
            aboutInfo_textView_other_user_profile.setText(userAbout)
        } else {
            aboutInfo_textView_other_user_profile.setText(dummyText)
        }


        //Try to load photo into image view
        val circleImageView = circle_image_view_other_user_profile

        if (!user.photo.isEmpty()){
            Picasso.get().load(user.photo).into(circle_image_view_other_user_profile)
            Log.d(TAG, "Photo is not empty \n" +
                    "${user.photo}")
        } else {
            circleImageView.setImageResource(R.drawable.profile_avatar_placeholder_large)
            Log.d(TAG,"user photo empty \n" +
                    "${user.photo}")
        }


    }
}
