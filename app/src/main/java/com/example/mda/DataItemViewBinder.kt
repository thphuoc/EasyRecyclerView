package com.example.mda

import android.content.Context
import android.view.View
import com.thphuoc.erv.EasyItemViewBinder
import kotlinx.android.synthetic.main.item_view.view.*

data class DataItemViewBinder(
    val context: Context,
    val name: String,
    val onRemove: (viewBinder: DataItemViewBinder) -> Unit
) : EasyItemViewBinder {
    override val viewResId: Int = R.layout.item_view

    override fun bind(view: View) {
        view.tvName.text = name
        view.btnDelete.setOnClickListener {
            onRemove(this)
        }
    }
}