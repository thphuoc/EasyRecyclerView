package com.example.mda

import android.content.Context
import android.view.View
import com.thphuoc.erv.EasyItemViewBinder
import kotlinx.android.synthetic.main.item_view_header.view.*

data class HeaderItemViewBinder(
    val context: Context,
    val name: String
) : EasyItemViewBinder {
    override val viewResId: Int = R.layout.item_view_header

    override fun bind(view: View) {
        view.tvName.text = name
    }
}