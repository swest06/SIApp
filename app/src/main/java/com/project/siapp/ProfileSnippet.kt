package com.project.siapp

import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder

class ProfileSnippet : Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
    //called in recycler view for each profile snippet
    }

    override fun getLayout(): Int {

        //return a layout file R.layout.?
        return R.layout.profile_snippet_row_search
    }
}