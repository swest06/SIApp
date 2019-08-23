package com.project.siapp

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class GroupActivity: AppCompatActivity(){
    private val textView by lazy { findViewById<TextView>(R.id.textView_group) }
    private val constraintLayout by lazy { findViewById<ConstraintLayout>(R.id.constraintLayout_activity_group) }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("GroupActivity", "Inside onCreate")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)
        supportActionBar?.title = "Group Events"

        //Animated gradient background
        animateBackground(constraintLayout)

        val unicode = 0x1F60A
        val emoji = getEmoji(unicode)
        textView.setText("Plan your group events " + emoji + " \n \n COMING SOON!")

    }

    private fun getEmoji(unicode: Int): String {
        return String(Character.toChars(unicode))
    }
}