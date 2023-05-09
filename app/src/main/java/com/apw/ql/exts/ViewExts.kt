package com.apw.ql.exts

import androidx.recyclerview.widget.RecyclerView

inline fun RecyclerView.onScrollToBottom(crossinline block: () -> Unit) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (!recyclerView.canScrollVertically(1)) {
                block()
            }
        }
    })
}