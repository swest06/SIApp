package com.project.siapp

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProfilePageActivity: AppCompatActivity() {
    private val TAG = "ProfilePageActivity"
    private val photo_button  by lazy { findViewById<Button>(R.id.photo_button_profile_page) }
    private val inbox_button by lazy { findViewById<Button>(R.id.inbox_button_profile_page) }
    private val search_button by lazy { findViewById<Button>(R.id.search_button_profile_page) }
    private val name_view by lazy { findViewById<TextView>(R.id.name_edit_text_profile_page) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)

        //Photo Button
        photo_button.setOnClickListener {
            Log.d(TAG, "photo buton clicked")
        }
    }
}