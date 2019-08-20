package com.project.siapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_inbox.*

class InboxActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inbox)
        supportActionBar?.title = "Inbox"

        setupDRows()
    }

    private fun setupDRows() {
        val adapter = GroupAdapter<ViewHolder>()


        recycler_view_inbox.adapter = adapter
    }

    class InboxRow: Item<ViewHolder>(){
        override fun getLayout(): Int {
            return R.layout.inbox_row
        }

        override fun bind(viewHolder: ViewHolder, position: Int) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }
}
