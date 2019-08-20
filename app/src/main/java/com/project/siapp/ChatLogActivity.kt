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

        //user Id's. Make global variable later!
        val fromId = FirebaseAuth.getInstance().uid
        val toId = user?.uid

        //Database reference
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")

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
        //Original Database reference
//        val ref = FirebaseDatabase.getInstance().getReference("/messages").push()

        //Parameters to be passed into message constructor
        val text = editText_chat_log.text.toString()
        val fromId = FirebaseAuth.getInstance().uid
        val toId = user.uid
        val timeStamp = System.currentTimeMillis() / 1000

        //check
        if (fromId == null) {
            return
        }

        //Database reference that creates new nodes for each new chats
        val ref1 = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()
        val ref2 = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()
        val inboxRef1 = FirebaseDatabase.getInstance().getReference("/inbox/$fromId/$toId")
        val inboxRef2 = FirebaseDatabase.getInstance().getReference("/inbox/$toId/$fromId")

        val message = Message(ref1.key!!, text, fromId!!, toId!!, timeStamp)

        //save message object to messages node in database (USER 1)
        ref1.setValue(message)
            .addOnSuccessListener {
                Log.d(TAG, "Message saved to 1st database ref: ${ref1.key}")

                //clear text from editText
                editText_chat_log.text.clear()

                //scrolls to last message sent
                reyclerView_chat_log.scrollToPosition(adapter.itemCount - 1)
            }
            .addOnFailureListener {
                //ADD A TOAST (MESSAGE COULD NOT BE SENT) WHEN YOU HAVE TIME!
            }

        //save message object to messages node in database (USER 2)
        ref2.setValue(message)
            .addOnSuccessListener {
                Log.d(TAG, "Message saved to 2nd database ref: ${ref2.key}")
            }

        //save messages to inbox node (Set on successlistenrs etc if have time)
        inboxRef1.setValue(message)
        inboxRef2.setValue(message)
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
