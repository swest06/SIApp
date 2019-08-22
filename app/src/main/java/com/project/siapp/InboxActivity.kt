package com.project.siapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_inbox.*
import kotlinx.android.synthetic.main.inbox_row.view.*

class InboxActivity : AppCompatActivity() {
    val userId by lazy { FirebaseAuth.getInstance().uid }
    val adapter = GroupAdapter<ViewHolder>()
    val messagesMap = HashMap<String, Message>()

    val TAG = "Inbox Activity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inbox)
        supportActionBar?.title = "Inbox"

        //set recycler view adapter
        recycler_view_inbox.adapter = adapter
        recycler_view_inbox.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        listenForMessages()

        adapter.setOnItemClickListener { item, view ->

            val intent = Intent(this, ChatLogActivity::class.java)

            val row = item as InboxRow

            intent.putExtra(SearchActivity.USER_KEY, row.otherUser)
            startActivity(intent)
        }

    }
    private fun refreshRecycler(){
        Log.d(TAG, "Inside refreshRecycler")

        adapter.clear()
        messagesMap.values.forEach{
            adapter.add((InboxRow(it)))
        }
    }

    private fun listenForMessages() {
        Log.d(TAG, "Inside listenForMessages")
        val ref = FirebaseDatabase.getInstance().getReference("/inbox/$userId")

        //listen for changes in child nodes
        ref.addChildEventListener(object: ChildEventListener{


            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                Log.d(TAG, "Inside onChildAdded")

                val message = p0.getValue(Message::class.java) ?: return
                messagesMap[p0.key!!] = message
                refreshRecycler()

            }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                Log.d(TAG, "Inside onChildChanged")

                val message = p0.getValue(Message::class.java) ?: return
                messagesMap[p0.key!!] = message
                refreshRecycler()
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onChildRemoved(p0: DataSnapshot) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }


    class InboxRow(val message: Message): Item<ViewHolder>(){
        var otherUser: User? = null

        override fun getLayout(): Int {
            return R.layout.inbox_row
        }

        override fun bind(viewHolder: ViewHolder, position: Int) {
            viewHolder.itemView.latest_message_textview_inbox_row.text = message.text

            //Get correct id for chat recipient
            val recipientId: String
            if  (message.fromId == FirebaseAuth.getInstance().uid){
                recipientId = message.toId
            } else {
                recipientId = message.fromId
            }

            //get recipient name from database reference
            val recipientRef = FirebaseDatabase.getInstance().getReference("/users/$recipientId")
            recipientRef.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    otherUser = p0.getValue(User::class.java)
                    viewHolder.itemView.username_textview_inbox_row.text = otherUser?.name

                    val imageView = viewHolder.itemView.imageview_inbox
                    Picasso.get().load(otherUser?.photo).into(imageView)
                }
            })

        }

    }
}
