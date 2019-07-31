package com.project.siapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class OtherUserProfileActivity : AppCompatActivity() {
    private val messageButton by lazy { findViewById<Button>(R.id.message_button_other_user_profile) }
    private val addToGroupButton by lazy { findViewById<Button>(R.id.add_to_group_button_other_user_profile) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_user_profile)
        supportActionBar?.title = "Profile"

        //Start chat log
        messageButton.setOnClickListener {
            startChatLog()
        }
    }

    fun startChatLog(){
        val intent = Intent(this, ChatLogActivity::class.java)
        startActivity(intent)
    }
}
