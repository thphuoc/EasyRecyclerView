package com.thphuoc.erv

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import kotlin.math.max


class EasyRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RecyclerView(context, attrs) {
    private val mAdapter by lazy { EasyListAdapter(context) }
    var enableLoadMore = false
    private var isLoadingMore = false
    private var loadMoreViewBinder = LoadMoreViewBinder {}

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.EasyRecyclerView,
            0, 0
        ).apply {

            if (hasValue(R.styleable.EasyRecyclerView_layout_type)) {
                val layoutType = getInt(R.styleable.EasyRecyclerView_layout_type, 0)
                Log.d("DEBUG", "layoutType: $layoutType")
                layoutManager = when (layoutType) {
                    1 -> {
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    }
                    2 -> {
                        GridLayoutManager(context, 2, VERTICAL, false)
                    }
                    else -> LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                }
            }

            recycle()
        }
    }

    fun disableRecycled(layoutResId: Int) {
        recycledViewPool.setMaxRecycledViews(layoutResId, 0)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        this.adapter = mAdapter

        handleLoadMore()
    }

    private fun handleLoadMore() {
        setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (!enableLoadMore) return@setOnScrollChangeListener
            var canScroll = canScrollVertically(1)
            if (!canScroll) {
                canScroll = canScrollHorizontally(1)
            }
            if (canScroll && !isLoadingMore) {
                isLoadingMore = true
                addItem(loadMoreViewBinder)
                loadMoreViewBinder.loadMore()
            }
        }
    }

    fun setLoadMore(loadMoreViewBinder: LoadMoreViewBinder) {
        enableLoadMore = true
        this.loadMoreViewBinder = loadMoreViewBinder
    }

    fun loadCompleted() {
        isLoadingMore = false
        removeItem(loadMoreViewBinder)
    }

    fun noMoreToLoad() {
        enableLoadMore = false
        removeItem(loadMoreViewBinder)
    }

    fun addItem(data: EasyItemViewBinder, index: Int = mAdapter.itemCount) {
        mAdapter.addItem(data, index)
        mAdapter.notifyItemInserted(max(0, index))
    }

    fun clear() {
        mAdapter.removeAll()
        mAdapter.notifyItemRangeRemoved(0, mAdapter.itemCount)
    }

    fun removeItem(data: EasyItemViewBinder) {
        val index = mAdapter.removeItem(data)
        if (index >= 0) {
            mAdapter.notifyItemRemoved(index)
        }
    }

    fun size() = mAdapter.itemCount
}