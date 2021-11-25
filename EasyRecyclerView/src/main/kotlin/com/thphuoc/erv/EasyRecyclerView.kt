package com.thphuoc.erv

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.recyclerview.widget.*
import com.example.myapplication.R
import kotlin.math.max


class EasyRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RecyclerView(context, attrs) {
    private val mAdapter by lazy { EasyListAdapter(context) }
    var enableLoadMore = false
    private var isLoadingMore = false
    private var loadMoreViewBinder = LoadMoreViewBinder {}
    private val VERTICAL = 0
    private val HORIZONTAL = 1
    private val VGRID2 = 2
    private val VGRID3 = 3
    private val VGRID4 = 4
    private val HGRID2 = 5
    private val HGRID3 = 6
    private val HGRID4 = 7

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
                    VERTICAL -> {
                        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    }
                    HORIZONTAL -> {
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    }
                    VGRID2 -> {
                        GridLayoutManager(context, VGRID2, RecyclerView.VERTICAL, false).apply {
                            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                                override fun getSpanSize(position: Int): Int {
                                    return when (getViewBinderAtPosition(position)) {
                                        is LoadMoreViewBinder -> VGRID2
                                        else -> 1
                                    }
                                }
                            }
                        }
                    }
                    VGRID3 -> {
                        GridLayoutManager(context, VGRID3, RecyclerView.VERTICAL, false).apply {
                            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                                override fun getSpanSize(position: Int): Int {
                                    return when (getViewBinderAtPosition(position)) {
                                        is LoadMoreViewBinder -> VGRID3
                                        else -> 1
                                    }
                                }
                            }
                        }
                    }
                    VGRID4 -> {
                        GridLayoutManager(context, VGRID4, RecyclerView.VERTICAL, false).apply {
                            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                                override fun getSpanSize(position: Int): Int {
                                    return when (getViewBinderAtPosition(position)) {
                                        is LoadMoreViewBinder -> VGRID4
                                        else -> 1
                                    }
                                }
                            }
                        }
                    }
                    HGRID2 -> {
                        GridLayoutManager(context, 2, RecyclerView.HORIZONTAL, false)
                    }
                    HGRID3 -> {
                        GridLayoutManager(context, 3, RecyclerView.HORIZONTAL, false)
                    }
                    HGRID4 -> {
                        GridLayoutManager(context, 4, RecyclerView.HORIZONTAL, false)
                    }
                    else -> LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
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
            if (!canScroll && !isLoadingMore) {
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

    fun getViewBinderAtPosition(position: Int): EasyItemViewBinder? {
        if (position >= 0 && position < mAdapter.size()) {
            return mAdapter.getItemAtPosition(position)
        }
        return null
    }

    fun filterBy(
        condition: (item: EasyItemViewBinder) -> Boolean
    ) {
        if (enableLoadMore) {
            noMoreToLoad()
            return
        }
        val newList = mAdapter.filter {
            if (it !is LoadMoreViewBinder) {
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