package com.thphuoc.erv

import android.view.View

interface EasyItemViewBinder {
    val viewResId: Int
    fun isRecyclable() = false

    fun bind(view: View) {}
}