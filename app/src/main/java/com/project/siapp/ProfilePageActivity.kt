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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class ProfilePageActivity: AppCompatActivity() {
    private val TAG = "ProfilePageActivity"
    private val photoButton  by lazy { findViewById<Button>(R.id.photo_button_profile_page) }
    private val inboxButton by lazy { findViewById<Button>(R.id.inbox_button_profile_page) }
    private val searchButton by lazy { findViewById<Button>(R.id.search_button_profile_page) }
    private val nameView by lazy { findViewById<TextView>(R.id.name_edit_text_profile_page) }
    private val circleImage by lazy { findViewById<CircleImageView>(R.id.circle_image_view_profile) }
    private val signOutButton by lazy { findViewById<Button>(R.id.sign_out_profile_page) }
    private lateinit var toast: Toast
    var photoUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)
        supportActionBar?.title = "Profile"
        toast = getToast(this@ProfilePageActivity)


        //CODE FOR LAUNCHING REGISTER PAGE WHEN USER IS NOT LOGGED IN. Uncomment when launcher intent filter is added to manifest xml
        //loginCheck()

        //Photo Button
        photoButton.setOnClickListener {
            photoButton()
        }

        //Sign Out Button
        signOutButton.setOnClickListener {
            signOut()
        }

        //Inbox Button
        inboxButton.setOnClickListener {
            goToInbox()
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
            circleImage.setImageBitmap(bitmap)

            //hide button after image upload
            photoButton.alpha = 0f

//            val bitmapDrawable = BitmapDrawable(bitmap)
//            photo_button.setBackgroundDrawable(bitmapDrawable)
        }
    }

    /**
     * Store image in Firebase storage
     */
    private fun uploadImage() {
        //check value
        if (photoUri == null){
            toast.setText("Photo could not be uploaded")
            toast.show()
            return
        }

        //Generate unique identifier for photo
        val filename = UUID.randomUUID().toString()

        //reference to Firebase storage location
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        //put photo into storage
        ref.putFile(photoUri!!)
            .addOnSuccessListener {
                Log.d(TAG, "Uploaded image Successfully: ${it.metadata?.path}")

                //retrieve file location
                ref.downloadUrl.addOnSuccessListener {
                    Log.d(TAG, "Storage location $it")
                    it.toString()
                }
            }
    }

    /**
     * When user wants to upload photo
     */
    private fun photoButton(){
        Log.d(TAG, "photo button clicked")

        //bring up photo selector
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 0)

        //Upload image to Firebase Storage
        uploadImage()
    }

    /**
     * Checks if a user is already logged in when launching app
     */
    private fun loginCheck(){
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null){
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    /**
     * Signs out user
     */
    private fun signOut(){
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    /**
     * Goes to user's inbox
     */
    private fun goToInbox(){
        val intent = Intent(this, InboxActivity::class.java)
        startActivity(intent)
    }
}