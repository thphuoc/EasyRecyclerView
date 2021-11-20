package com.example.mda

import android.view.View
import com.thphuoc.erv.EasyItemViewBinder
import kotlinx.android.synthetic.main.hoz_list_view.view.*

class HozListViewBinder : EasyItemViewBinder {
    override val viewResId: Int = R.layout.hoz_list_view

    override fun bind(view: View) {
        view.hozList.clear()
        repeat(10) {
            view.hozList.addItem(HozItemViewBinder())
        }
    }
}