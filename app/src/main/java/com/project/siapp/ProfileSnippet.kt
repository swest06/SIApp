package com.project.siapp

import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.profile_snippet_row_search.view.*

class ProfileSnippet(val user: User) : Item<ViewHolder>() {

    /**
     * called in recycler view for each profile snippet
     */
    override fun bind(viewHolder: ViewHolder, position: Int) {

        //sets name for each user
        viewHolder.itemView.name_textView_profile_snippet_row_search.text = user.name

        //sets image for each user (user.photo needs refactoring)!
        Picasso.get().load(user.photo).into(viewHolder.itemView.circle_image_view_profile)

    }

    override fun getLayout(): Int {

        //return a layout file R.layout.?
        return R.layout.profile_snippet_row_search
    }
}
