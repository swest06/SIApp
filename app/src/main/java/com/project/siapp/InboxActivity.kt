package com.project.siapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_inbox.*
import kotlinx.android.synthetic.main.inbox_row.view.*

class InboxActivity : AppCompatActivity() {
    val userId by lazy { FirebaseAuth.getInstance().uid }
    val adapter = GroupAdapter<ViewHolder>()
    val messagesMap = HashMap<String, Message>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inbox)
        supportActionBar?.title = "Inbox"

        //set recycler view adapter
        recycler_view_inbox.adapter = adapter

        listenForMessages()
    }
    private fun refreshRecycler(){
        adapter.clear()
        messagesMap.values.forEach{
            adapter.add((InboxRow(it)))
        }
    }

    private fun listenForMessages() {

        val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$userId")

        //listen for changes in child nodes
        ref.addChildEventListener(object: ChildEventListener{

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val message = p0.getValue(Message::class.java) ?: return
                messagesMap[p0.key!!] = message
                refreshRecycler()

            }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
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
        override fun getLayout(): Int {
            return R.layout.inbox_row
        }

        override fun bind(viewHolder: ViewHolder, position: Int) {
            viewHolder.itemView.latest_message_textview_inbox_row.text = message.text
        }

    }
}
