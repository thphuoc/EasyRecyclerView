package com.thphuoc.erv

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class EasyListAdapter(private val context: Context) : RecyclerView.Adapter<EasyViewHolder>() {

    private val viewBinders = arrayListOf<EasyItemViewBinder>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EasyViewHolder {
        return EasyViewHolder(LayoutInflater.from(context).inflate(viewType, parent, false))
    }

    override fun getItemViewType(position: Int): Int {
        return viewBinders[position].viewResId
    }

    override fun onViewRecycled(holder: EasyViewHolder) {
        holder.onViewDetached()
    }

    override fun onBindViewHolder(holder: EasyViewHolder, position: Int) {
        holder.onViewAttached(viewBinders[position])
    }

    override fun getItemCount(): Int = viewBinders.size

    fun addItem(data: EasyItemViewBinder, index: Int = viewBinders.size) {
        viewBinders.add(index, data)
    }

    fun removeItem(data: EasyItemViewBinder): Int {
        val itemIndex = viewBinders.indexOf(data)
        if (itemIndex >= 0) {
            viewBinders.remove(data)
        }
        return itemIndex
    }

    fun removeAll() {
        viewBinders.clear()
    }
}