package com.thphuoc.erv

import android.view.View
import androidx.recyclerview.widget.RecyclerView

class EasyViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    private var mData: EasyItemViewBinder? = null

    fun onViewAttached(data: EasyItemViewBinder) {
        mData = data
        data.bind(view)
    }

    fun onViewDetached() {
        mData?.unbind()
    }
}