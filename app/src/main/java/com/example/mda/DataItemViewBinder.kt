package com.example.mda

import android.content.Context
import android.view.View
import com.thphuoc.erv.EasyItemViewBinder
import kotlinx.android.synthetic.main.item_view.view.*

data class DataItemViewBinder(
    val context: Context,
    val user: UserDAO,
    val onRemove: (viewBinder: DataItemViewBinder) -> Unit
) : EasyItemViewBinder {
    override val viewResId: Int = R.layout.item_view

    override fun bind(view: View) {
        view.tvName.text = user.name
        view.btnDelete.setOnClickListener {
            onRemove(this)
        }
    }

    override fun sameContentWith(item: EasyItemViewBinder): Boolean {
        return if(item is DataItemViewBinder) user.name == item.user.name else false
    }
}