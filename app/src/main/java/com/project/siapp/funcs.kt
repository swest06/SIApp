package com.project.siapp

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.AnimationDrawable
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout

/**
 * Creates moving gradient background by fading in/out multiple backgrounds
 */
fun animateBackground(constraintLayout: ConstraintLayout){
    val animationDrawable: AnimationDrawable = constraintLayout.background as AnimationDrawable
    animationDrawable.setEnterFadeDuration(2000)
    animationDrawable.setExitFadeDuration(4000)
    animationDrawable.start()
}

/**
 * Creates toast of one style that all classes can use
 */
fun getToast(context: Context) : Toast{
    var toast = Toast.makeText(context, "", Toast.LENGTH_SHORT)
    toast.setGravity(Gravity.CENTER, 0, -135)
    val view = toast.view

    //Gets the actual oval background of the Toast then sets the colour filter
    view.background.setColorFilter(Color.rgb(105,0,162), PorterDuff.Mode.SRC_IN)

    //Gets the TextView from the Toast so it can be editted
    val text = view.findViewById<TextView>(android.R.id.message)
    text.setTextColor(Color.WHITE)

    return toast
}