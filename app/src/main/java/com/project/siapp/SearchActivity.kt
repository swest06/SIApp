package com.project.siapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        supportActionBar?.title = "Search Users"

        //setup adapter. Might need to make a custom adapter class that extends RecyclerView.Adapter<ViewHolder>
        recycler_view_search.adapter
        recycler_view_search.layoutManager = LinearLayoutManager(this) //can be added in xml instead
    }
}
