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
    private val photo_button  by lazy { findViewById<Button>(R.id.photo_button_profile_page) }
    private val inbox_button by lazy { findViewById<Button>(R.id.inbox_button_profile_page) }
    private val search_button by lazy { findViewById<Button>(R.id.search_button_profile_page) }
    private val name_view by lazy { findViewById<TextView>(R.id.name_edit_text_profile_page) }
    private val circleImage by lazy { findViewById<CircleImageView>(R.id.circle_image_view_profile) }
    private lateinit var toast: Toast
    var photoUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)
        toast = getToast(this@ProfilePageActivity)


        //CODE FOR LAUNCHING REGISTER PAGE WHEN USER IS NOT LOGGED IN. Uncomment when launcher intent filter is added to manifest xml
        //loginCheck()

        //Photo Button
        photo_button.setOnClickListener {
            photoButton()
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
            photo_button.alpha = 0f

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

    private fun photoButton(){
        Log.d(TAG, "photo button clicked")

        //bring up photo selector
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 0)

        //Upload image to Firebase Storage
        uploadImage()
    }

    private fun loginCheck(){
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null){
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}