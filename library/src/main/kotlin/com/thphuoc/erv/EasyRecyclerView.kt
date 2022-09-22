package com.thphuoc.erv

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.recyclerview.widget.DiffUtil
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
    private var currentItemDecoration: GridSpacingItemDecoration? = null
    private var layoutType: LayoutType = LayoutType.VERTICAL
    private var onMovedItemListener: (from: Int, to: Int) -> Unit = { _, _ -> }

    enum class LayoutType(val value: Int, val spanCount: Int) {
        VERTICAL(0, 1),
        HORIZONTAL(1, 1),
        VGRID2(2, 2),
        VGRID3(3, 3),
        VGRID4(4, 4),
        HGRID2(5, 2),
        HGRID3(6, 3),
        HGRID4(7, 4);

        companion object {
            fun getType(index: Int) = values().firstOrNull { it.value == index } ?: VERTICAL
        }
    }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.EasyRecyclerView,
            0, 0
        ).apply {

            val layoutTypeIndex = getInt(R.styleable.EasyRecyclerView_layout_type, 0)
            layoutType = LayoutType.getType(layoutTypeIndex)
            setLayoutType(layoutType)

            if (hasValue(R.styleable.EasyRecyclerView_item_space)) {
                val space = getDimensionPixelSize(R.styleable.EasyRecyclerView_item_space, 0)
                val includeEdgeSpace =
                    getBoolean(R.styleable.EasyRecyclerView_include_edge_space, false)


                currentItemDecoration?.apply { removeItemDecoration(this) }
                currentItemDecoration =
                    GridSpacingItemDecoration(layoutType.spanCount, space, includeEdgeSpace)
                addItemDecoration(currentItemDecoration!!)
            }
            val draggable = getBoolean(R.styleable.EasyRecyclerView_draggable, false)
            if (draggable) {
                setDraggable()
            }
            recycle()
        }
    }

    fun setDecoration(space: Int, includeEdgeSpace: Boolean) {
        currentItemDecoration?.apply { removeItemDecoration(this) }
        currentItemDecoration =
            GridSpacingItemDecoration(layoutType.spanCount, space, includeEdgeSpace)
        addItemDecoration(currentItemDecoration!!)
    }

    fun setDraggable() {
        mAdapter.setDraggable(this)
    }

    fun moveItem(from: Int, to: Int) {
        mAdapter.moveItem(from, to)
        onMovedItemListener(from, to)
    }

    fun setOnItemMoved(onItemMoved: (from: Int, to: Int) -> Unit) {
        this.onMovedItemListener = onItemMoved
    }

    fun setLayoutType(layoutType: LayoutType) {
        this.layoutType = layoutType
        layoutManager = when (layoutType) {
            LayoutType.VERTICAL -> {
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }
            LayoutType.HORIZONTAL -> {
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
            LayoutType.VGRID2,
            LayoutType.VGRID3,
            LayoutType.VGRID4 -> {
                GridLayoutManager(context, layoutType.spanCount, VERTICAL, false).apply {
                    spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                        override fun getSpanSize(position: Int): Int {
                            return when (getViewBinderAtPosition(position)) {
                                is LoadMoreViewBinder,
                                is VerticalSectionItemViewBinder -> layoutType.spanCount
                                else -> 1
                            }
                        }
                    }
                }
            }
            LayoutType.HGRID2,
            LayoutType.HGRID3,
            LayoutType.HGRID4 -> {
                GridLayoutManager(context, layoutType.spanCount, HORIZONTAL, false).apply {
                    spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                        override fun getSpanSize(position: Int): Int {
                            return when (getViewBinderAtPosition(position)) {
                                is LoadMoreViewBinder -> layoutType.spanCount
                                else -> 1
                            }
                        }
                    }
                }
            }
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

    fun getAllViewBinders(): List<EasyItemViewBinder> {
        return mAdapter.getAllItems()
    }

    private fun handleLoadMore() {
        setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (!enableLoadMore) return@setOnScrollChangeListener

            val lastItemVisible =
                (layoutManager as? LinearLayoutManager)?.findLastCompletelyVisibleItemPosition()
                    ?: (layoutManager as? GridLayoutManager)?.findFirstVisibleItemPosition()
                    ?: 0
            val firstItemVisible = (layoutManager as? LinearLayoutManager)?.findFirstCompletelyVisibleItemPosition()
                ?: (layoutManager as? GridLayoutManager)?.findLastVisibleItemPosition()
                ?: 0

            if (lastItemVisible == mAdapter.size() - (lastItemVisible - firstItemVisible) && !isLoadingMore) {
                isLoadingMore = true
                addLoadMoreItem(loadMoreViewBinder, mAdapter.itemCount)
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

    private fun addLoadMoreItem(data: EasyItemViewBinder, index: Int = mAdapter.itemCount) {
        mAdapter.addItem(data, index)
        post { mAdapter.notifyItemInserted(max(0, index)) }
    }

    fun addAllItems(data: List<EasyItemViewBinder>, index: Int = mAdapter.itemCount) {
        mAdapter.addAllItems(data, index)
        mAdapter.notifyItemRangeInserted(index, mAdapter.itemCount)
    }

    fun clear() {
        mAdapter.removeAll()
        mAdapter.notifyDataSetChanged()
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

    fun updateViewBinders(newList: List<EasyItemViewBinder>) {
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = mAdapter.size()

            override fun getNewListSize(): Int = newList.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return mAdapter.getItemAtPosition(oldItemPosition)
                    .sameContentWith(newList[newItemPosition])
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return mAdapter.getItemAtPosition(oldItemPosition)
                    .sameContentWith(newList[newItemPosition])
            }
        })

        diffResult.dispatchUpdatesTo(mAdapter)
        mAdapter.update(newList)
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