package com.thphuoc.erv

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.max

class EasyRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RecyclerView(context, attrs) {
    private val mAdapter by lazy { EasyListAdapter(context) }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        this.adapter = mAdapter
    }

    fun addItem(data: EasyItemViewBinder, index: Int = mAdapter.itemCount) {
        mAdapter.addItem(data, index)
        mAdapter.notifyItemInserted(max(0, index))
    }

    fun removeItem(data: EasyItemViewBinder) {
        mAdapter.notifyItemRemoved(mAdapter.removeItem(data))
    }

    fun size() = mAdapter.itemCount
}