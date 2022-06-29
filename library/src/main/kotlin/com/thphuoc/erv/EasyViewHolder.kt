package com.thphuoc.erv

import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class EasyViewHolder(private val view: View, private val touchHelper: ItemTouchHelper? = null) : RecyclerView.ViewHolder(view) {
    private var mData: EasyItemViewBinder? = null

    fun onViewAttached(data: EasyItemViewBinder) {
        mData = data
        data.bind(view)
        data.getDraggableHandler()?.apply {
            view.findViewById<View>(this)?.setOnTouchListener { v, event ->
                when (event.actionMasked) {
                    MotionEvent.ACTION_UP -> v.performClick()
                    MotionEvent.ACTION_DOWN -> {
                        touchHelper?.startDrag(this@EasyViewHolder)
                    }
                }

                true
            }
        }
    }

    fun onViewDetached() {
        mData?.unbind()
    }
}