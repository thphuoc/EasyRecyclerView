package com.thphuoc.erv

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class EasyListAdapter(private val context: Context) : RecyclerView.Adapter<EasyViewHolder>() {

    val datas = arrayListOf<EasyItemViewBinder>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EasyViewHolder {
        return EasyViewHolder(LayoutInflater.from(context).inflate(viewType, parent, false))
    }

    override fun getItemViewType(position: Int): Int {
        return datas[position].viewResId
    }

    override fun onBindViewHolder(holder: EasyViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    override fun getItemCount(): Int = datas.size

    fun addItem(data: EasyItemViewBinder, index: Int = datas.size) {
        datas.add(index, data)
    }

    fun removeItem(data: EasyItemViewBinder) : Int {
        val itemIndex = datas.indexOf(data)
        datas.remove(data)
        return itemIndex
    }
}