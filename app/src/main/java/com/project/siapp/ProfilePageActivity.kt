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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_profile_page.*
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

    private val user by lazy { FirebaseAuth.getInstance().currentUser}
    private val database by lazy { FirebaseDatabase.getInstance().reference }

    companion object{
        var currentUser: User? = null
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)
        supportActionBar?.title = "Profile"
        toast = getToast(this@ProfilePageActivity)

        photoButton.translationZ = 10F
        photoButton.alpha = 1F


        //Set profile values
        setProfileValues(user, database)

        //CODE FOR LAUNCHING REGISTER PAGE WHEN USER IS NOT LOGGED IN. Uncomment when launcher intent filter is added to manifest xml
        //loginCheck()

        //Save Button
        saveButton.setOnClickListener {
            saveChanges(user, database)
            setProfileValues(user, database)
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

    private fun setProfileValues(user: FirebaseUser?, database: DatabaseReference) {
        val userRef = database.child("users").child(user?.uid!!)
        userRef.addListenerForSingleValueEvent(object: ValueEventListener{

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "\n profile changes could not made \n" +
                        "DatabaseError: ${error}")
            }

            //Get node values
            override fun onDataChange(snapshot: DataSnapshot) {
                val name: String? = snapshot.child("name").getValue(String::class.java)
                val location: String? = snapshot.child("location").getValue(String::class.java)
                val age: String? = snapshot.child("age").getValue(String::class.java)
                val gender: String? = snapshot.child("gender").getValue(String::class.java)
                val aboutInfo: String? = snapshot.child("about").getValue(String::class.java)
                val photo: String? = snapshot.child("photo").getValue(String::class.java)

                //try to get whole current user
                currentUser = snapshot.getValue(User::class.java)

                //set activity text fields
                name_textView_profile_page.setText(name)
                location_textView_profile_page.setText(location)
                age_textView_profile_page.setText(age)
                gender_textView_profile_page.setText(gender)
                aboutInfo_textView_profile_page.setText(aboutInfo)

                //set profile photo
                if (!photo.isNullOrEmpty()){
                    Picasso.get().load(photo).into(circle_image_view_profile_page)
                    photoButton.alpha = 0F
                } else {
                    Log.d(TAG, "Photo is null")
                    photoButton.translationZ = 10F
                    photoButton.alpha = 1F
                }

            }
        })
    }

    private fun saveChanges(user: FirebaseUser?, database: DatabaseReference){

        if (user != null) {

            database.child("users").child(user.uid).child("name").setValue(name_textView_profile_page.text.toString())
            database.child("users").child(user.uid).child("age").setValue(age_textView_profile_page.text.toString())
            database.child("users").child(user.uid).child("gender").setValue(gender_textView_profile_page.text.toString())
            database.child("users").child(user.uid).child("location").setValue(location_textView_profile_page.text.toString())
            database.child("users").child(user.uid).child("about").setValue(aboutInfo_textView_profile_page.text.toString())
                .addOnSuccessListener {
                    toast.setText("Changes saved")
                    toast.show()
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

        //goes to onActivityResult()
        startActivityForResult(intent, 0)

        //Upload image to Firebase Storage (trying inside above function
        //uploadImage()
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



            if(photoUri != null) {

                //Delete after
                print("photoUri not null ${photoUri.toString()}")
                Log.d(TAG, "\"photoUri not null ${photoUri.toString()}\"")

                //hide button after image upload
                photoButton.alpha = 0F

                //Upload image to firebase storage
                uploadImage()
            } else {
                print("photoUri is null!!: ${photoUri.toString()}")
                Log.d(TAG, "\"photoUri is null ${photoUri.toString()}\"")

                toast.setText("Photo could not be uploaded")
                toast.show()
                return
            }


//            val bitmapDrawable = BitmapDrawable(bitmap)
//            photo_button.setBackgroundDrawable(bitmapDrawable)
        }
    }

    /**
     * Store image in Firebase storage
     */
    private fun uploadImage() {

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