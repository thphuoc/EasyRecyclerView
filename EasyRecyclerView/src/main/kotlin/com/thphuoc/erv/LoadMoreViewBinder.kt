package com.thphuoc.erv

import com.example.myapplication.R

open class LoadMoreViewBinder(private val onLoadMore:()->Unit) : EasyItemViewBinder {
    override val viewResId: Int = R.layout.view_load_more

    fun loadMore() {
        this.onLoadMore()
    }
}