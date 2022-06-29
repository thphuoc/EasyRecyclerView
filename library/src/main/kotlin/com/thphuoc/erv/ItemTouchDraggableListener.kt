package com.thphuoc.erv

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

internal class ItemTouchDraggableListener : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,
    0
) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val erv = recyclerView as EasyRecyclerView
        val from = viewHolder.adapterPosition
        val to = target.adapterPosition
        // 2. Update the backing model. Custom implementation in
        //    MainRecyclerViewAdapter. You need to implement
        //    reordering of the backing model inside the method.
        erv.moveItem(from, to)

        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }
}