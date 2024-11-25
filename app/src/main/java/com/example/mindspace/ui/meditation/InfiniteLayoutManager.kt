package com.example.mindspace.ui.meditation

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class InfiniteLayoutManager(context: Context) : LinearLayoutManager(context, HORIZONTAL, false) {

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        val scrolled = super.scrollHorizontallyBy(dx, recycler, state)
        val childCount = childCount
        val itemCount = itemCount
        val firstVisiblePosition = findFirstVisibleItemPosition()
        val lastVisiblePosition = findLastVisibleItemPosition()

        if (childCount > 0) {
            if (dx > 0) { // Scrolling right
                if (lastVisiblePosition >= itemCount - 1) {
                    scrollToPositionWithOffset(0, 0)
                }
            } else { // Scrolling left
                if (firstVisiblePosition <= 0) {
                    scrollToPositionWithOffset(itemCount - 1, 0)
                }
            }
        }

        return scrolled
    }
}
