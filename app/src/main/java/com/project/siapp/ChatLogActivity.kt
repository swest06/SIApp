package com.project.siapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
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

    //parcelized user object
    val user by lazy { intent.getParcelableExtra<User>(OtherUserProfileActivity.USER_KEY) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)
        supportActionBar?.title = "${user.name}"

        val adapter = GroupAdapter<ViewHolder>()
        reyclerView_chat_log.adapter = adapter

        sendButton.setOnClickListener {
            Log.d(TAG, "Send button clicked")
            sendMessage()
        }
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
class Message(val id: String, val text: String, val fromId: String, val toId: String, val timeStamp: Long){

}


class ChatFromItem(val text: String): Item<ViewHolder>(){

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_chat_from.text = text
    }

    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }
}


class ChatToItem(val text: String): Item<ViewHolder>(){

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_chat_to.text = text
    }

    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}
