package com.thphuoc.erv

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.*
import com.example.myapplication.R
import java.lang.Exception
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

    fun addAllItems(data: List<EasyItemViewBinder>, index: Int = mAdapter.itemCount) {
        mAdapter.addAllItems(data, index)
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

    fun filterBy(
        condition: (item: EasyItemViewBinder) -> Boolean
    ) {
        if(enableLoadMore) {
            Exception("Please make sure no more to load.").printStackTrace()
            return
        }
        val newList = mAdapter.filter {
            if(it !is LoadMoreViewBinder) {
                condition(it)
            } else {
                true
            }
        }
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = mAdapter.size()

            override fun getNewListSize(): Int = newList.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return mAdapter.getItemAtPosition(oldItemPosition) === newList[newItemPosition]
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return mAdapter.getItemAtPosition(oldItemPosition)
                    .sameContentWith(newList[newItemPosition])
            }
        })

        diffResult.dispatchUpdatesTo(mAdapter)
        mAdapter.update(newList)

    }

    fun size() = mAdapter.itemCount
}