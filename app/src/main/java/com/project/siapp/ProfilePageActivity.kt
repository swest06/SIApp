package com.project.siapp

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ProfilePageActivity: AppCompatActivity() {
    private val TAG = "ProfilePageActivity"
    private val photo_button  by lazy { findViewById<Button>(R.id.photo_button_profile_page) }
    private val inbox_button by lazy { findViewById<Button>(R.id.inbox_button_profile_page) }
    private val search_button by lazy { findViewById<Button>(R.id.search_button_profile_page) }
    private val name_view by lazy { findViewById<TextView>(R.id.name_edit_text_profile_page) }
    private lateinit var toast: Toast
    var photoUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)
        toast = getToast(this@ProfilePageActivity)

        //Photo Button
        photo_button.setOnClickListener {
            Log.d(TAG, "photo button clicked")

            //bring up photo selector
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)

            //Upload image to Firebase Storage
            uploadImage()
        }
    }

    /**
     * Called when the intent(photo selector) has finished.
     * Gets notified with the 'data: Intent?' from the photo selector
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){

            //Log to check that a photo was selected
            Log.d(TAG, "Photo selected!")

            //URI represents location of where image is stored on device
            photoUri = data.data

            //Save image as bitmap and set as background within placeholder(Button)
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, photoUri)
            val bitmapDrawable = BitmapDrawable(bitmap)
            photo_button.setBackgroundDrawable(bitmapDrawable)
        }
    }

    private fun uploadImage(){

    }
}