package com.thphuoc.erv

import android.view.View
import androidx.recyclerview.widget.RecyclerView

class EasyViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    fun onViewAttached(data: EasyItemViewBinder) {
        data.bind(view)
    }

    fun onViewDetached() {}
}