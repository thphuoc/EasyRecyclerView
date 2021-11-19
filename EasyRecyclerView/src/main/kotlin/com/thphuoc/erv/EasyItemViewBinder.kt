package com.thphuoc.erv

import android.view.View

interface EasyItemViewBinder {
    val viewResId: Int

    fun bind(view: View)
}