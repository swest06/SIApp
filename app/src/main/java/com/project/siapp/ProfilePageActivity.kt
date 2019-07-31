package com.project.siapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class ProfilePageActivity: AppCompatActivity() {
    private val TAG = "ProfilePageActivity"
    private val saveButton by lazy {findViewById<Button>(R.id.save_changes_button_profile_page)}
    private val photoButton  by lazy { findViewById<Button>(R.id.photo_profile_page) }
    private val inboxButton by lazy { findViewById<Button>(R.id.inbox_button_profile_page) }
    private val searchButton by lazy { findViewById<Button>(R.id.search_button_profile_page) }
    private val nameView by lazy { findViewById<TextView>(R.id.name_textView_profile_page) }
    private val circleImage by lazy { findViewById<CircleImageView>(R.id.circle_image_view_profile_page) }
//    private val signOutButton by lazy { findViewById<Button>(R.id.sign_out_profile_page) }
    private lateinit var toast: Toast
    var photoUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)
        supportActionBar?.title = "Profile"
        toast = getToast(this@ProfilePageActivity)


        //CODE FOR LAUNCHING REGISTER PAGE WHEN USER IS NOT LOGGED IN. Uncomment when launcher intent filter is added to manifest xml
        //loginCheck()

        //Save Button
        saveButton.setOnClickListener {
            saveChanges()
        }

        //Photo Button
        photoButton.setOnClickListener {
            photoButton()
        }

        //Sign Out Button
//        signOutButton.setOnClickListener {
//            signOut()
//        }

        //Inbox Button
        inboxButton.setOnClickListener {
            goToInbox()
        }

        //Search Button
        searchButton.setOnClickListener {
            searchPage()
        }
    }

    private fun saveChanges(){
//        val id = FirebaseAuth.getInstance().uid ?: ""
//        val userIdReference = FirebaseDatabase.getInstance().getReference("/users/$id")

        val user = FirebaseAuth.getInstance().currentUser
        val database = FirebaseDatabase.getInstance().reference
        if (user != null) {

            //FIX HERE IT'S NOT GETTING THE CORRECT VALUES
            database.child("users").child(user.uid).child("name").setValue(R.id.name_textView_profile_page.toString())
            database.child("users").child(user.uid).child("age").setValue(R.id.age_textView_profile_page.toString())
            database.child("users").child(user.uid).child("gender").setValue(R.id.gender_textView_profile_page.toString())
            database.child("users").child(user.uid).child("location").setValue("London")
            database.child("users").child(user.uid).child("about").setValue(R.id.aboutInfo_textView_profile_page.toString())
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

        //goes to onActivityResult()
        startActivityForResult(intent, 0)

        //Upload image to Firebase Storage
        uploadImage()
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


                    //try this to add photo to user's node
                    val id = FirebaseAuth.getInstance().uid ?: ""
                    val userPhotoReference = FirebaseDatabase.getInstance().getReference("/users/$id/photo")
                    userPhotoReference.setValue(it.toString())
                        .addOnSuccessListener {
                            Log.d(TAG, "Image reference added to user's node")
                        }
                        .addOnFailureListener {
                            Log.d(TAG, "Image reference couldn't be added to users node \n" +
                                    "Trying again")

                            //or try this if above fails
                            val user = FirebaseAuth.getInstance().currentUser
                            val database = FirebaseDatabase.getInstance().reference
                            if (user != null) {
                                database.child("users").child(user.uid).child("photo").setValue(it.toString())
                                    .addOnSuccessListener {
                                        Log.d(TAG, "Image reference added to user's node")
                                    }
                                    .addOnFailureListener{
                                        Log.d(TAG, "Image reference still couldn't be added to users node!")
                                    }
                            }
                        }



                    //DELETE AFTER!
                    //experimental user update code(NEEDS TESTING!)
//                    val user = FirebaseAuth.getInstance().currentUser
//                    if (user != null) {
//
//                        // User is signed in
//                        val profileUpdates = UserProfileChangeRequest.Builder()
//                            .setPhotoUri(photoUri)
//                            .build()
//
//                        //code from firebase.google (May need refactoring)
//                        user?.updateProfile(profileUpdates)
//                            ?.addOnCompleteListener { task ->
//                                if (task.isSuccessful) {
//                                    Log.d(TAG, "User profile updated.")
//                                }
//                            }
//                    } else {
//                        // No user is signed in
//                    }
                }
            }
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
     * Add menu options to action bar
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.menu_sign_out){
            signOut()
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Signs out user
     */
    private fun signOut(){
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    /**
     * Goes to user's inbox
     */
    private fun goToInbox(){
        val intent = Intent(this, InboxActivity::class.java)
        startActivity(intent)
    }

    /**
     * Go to search page
     */
    private fun searchPage() {
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }
}