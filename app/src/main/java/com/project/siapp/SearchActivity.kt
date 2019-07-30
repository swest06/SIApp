package com.project.siapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {
    private val TAG = "SearchActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        supportActionBar?.title = "Search Users"

//        //setup adapter. Might need to make a custom adapter class that extends RecyclerView.Adapter<ViewHolder>
//        val adapter = GroupAdapter<ViewHolder>()
//
//        //Adding each profile snippet to recycler view(for testing purposes. Put in loop later)
//        adapter.add(ProfileSnippet())
//        adapter.add(ProfileSnippet())
//        adapter.add(ProfileSnippet())
//        adapter.add(ProfileSnippet())
//
//        recycler_view_search.adapter = adapter
        recycler_view_search.layoutManager = LinearLayoutManager(this) //can be added in xml instead

        //Retrieve user data
        retrieveUsers()
    }

    private fun retrieveUsers(){
        val reference = FirebaseDatabase.getInstance().getReference("/users")
        reference.addListenerForSingleValueEvent(object: ValueEventListener{

            //called once all users in db have been retrieved
            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()

                //for each user retrieved add a new profile snippet to the group adapter that will be rendered in the recycler view
                snapshot.children.forEach{
                    Log.d(TAG, it.toString())
                    val user = it.getValue(User::class.java)
                    if  (user != null){
                        adapter.add(ProfileSnippet(user))
                    }
                }

                //Go to other user's full profile
                adapter.setOnItemClickListener { item, view ->

                    val intent = Intent(view.context, OtherUserProfileActivity::class.java)
                    startActivity(intent)
                }


                recycler_view_search.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {
            //TODO
            }
        })
    }
}
