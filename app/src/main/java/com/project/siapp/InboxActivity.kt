package com.project.siapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class InboxActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inbox)
        supportActionBar?.title = "Inbox"
    }
}
