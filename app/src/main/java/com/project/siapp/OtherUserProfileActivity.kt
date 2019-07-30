package com.project.siapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class OtherUserProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_user_profile)
        supportActionBar?.title = "Profile"
    }
}
