package com.apw.ql

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

fun Matcher<View>.atPosition(position: Int): Matcher<View> {

    return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {

        override fun describeTo(description: Description?) {
            description?.appendText("has item at position $position: ");
            this@atPosition.describeTo(description)
        }

        override fun matchesSafely(view: RecyclerView?): Boolean {
            val viewHolder = view?.findViewHolderForAdapterPosition(position) ?: return false
            return this@atPosition.matches(viewHolder.itemView);
        }
    }
}