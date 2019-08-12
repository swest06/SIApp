package com.project.siapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*

class ChatLogActivity : AppCompatActivity() {
    val sendButton by lazy { findViewById<Button>(R.id.send_button_chat_log) }

    companion object{
        val TAG = "ChatLogActivity"
    }

    val userName by lazy { intent.getStringExtra(OtherUserProfileActivity.USER_NAME) }
    val userPhoto by lazy { intent.getStringExtra(OtherUserProfileActivity.USER_PHOTO) }

    //parcelized user object. Might change to lateinit var
    val user by lazy { intent.getParcelableExtra<User>(OtherUserProfileActivity.USER_KEY) }

    //adapter
    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)
        supportActionBar?.title = user.name


        reyclerView_chat_log.adapter = adapter

        //listener for new messages
        listenForMessages()

        //Send button
        sendButton.setOnClickListener {
            Log.d(TAG, "Send button clicked")
            sendMessage()
        }
    }

    private fun listenForMessages(){
        //Database reference
        val ref = FirebaseDatabase.getInstance().getReference("messages")

        //listens for changes to messages child nodes
        ref.addChildEventListener(object: ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            /**
             * Everytime a new node is added to the database add it to the adapter
             */
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val message= p0.getValue(Message::class.java)

                if (message != null){
                    Log.d(TAG, message?.text)

                    //add messages to adapter
                    if (message.fromId == FirebaseAuth.getInstance().uid){

                        val currentUser = ProfilePageActivity.currentUser
                        adapter.add(ChatFromItem(message.text, currentUser!!))
                    } else {
                        adapter.add(ChatToItem(message.text, user))
                    }
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }

    private fun sendMessage() {
        //Database reference
        val ref = FirebaseDatabase.getInstance().getReference("/messages").push()

        //Parameters to be passed into message constructor
        val text = editText_chat_log.text.toString()
        val fromId = FirebaseAuth.getInstance().uid
        val toId = user.uid
        val timeStamp = System.currentTimeMillis() / 1000

        val message = Message(ref.key!!, text, fromId!!, toId!!, timeStamp)

        //save message object to messages node in database
        ref.setValue(message)
            .addOnSuccessListener {
                Log.d(TAG, "Message saved to database: ${ref.key}")
            }
    }
}


/**
 * Classes used in this class. Eventually give their own file
 */


class ChatFromItem(val text: String, val user: User): Item<ViewHolder>(){

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_chat_from.text = text

        //Put profile picture in circle view
        val uri = user.photo
        val circleImageView = viewHolder.itemView.imageView_chat_from

        if (!uri.isEmpty()){
            Picasso.get().load(uri).into(circleImageView)
        } else {
            circleImageView.setImageResource(R.drawable.profile_avatar_placeholder_large)
        }

    }

    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }
}


class ChatToItem(val text: String, val user: User): Item<ViewHolder>(){

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_chat_to.text = text

        //Put profile picture in circle view
        val uri = user.photo
        val circleImageView = viewHolder.itemView.imageView_chat_to

        if (!uri.isEmpty()){
            Picasso.get().load(uri).into(circleImageView)
        } else {
            circleImageView.setImageResource(R.drawable.profile_avatar_placeholder_large)
        }

    }

    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}
