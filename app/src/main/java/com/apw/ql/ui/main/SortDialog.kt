package com.apw.ql.ui.main

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.apw.ql.R
import com.apw.ql.databinding.FragmentDialogFilterBinding

class SortDialog(private val onSortChanged: (sort: String?) -> Unit): DialogFragment() {
    companion object {
        private const val ARG_KEY_SORT = "com.apw.ql.arg_key_sort"

        const val CONDITION_MOST_STARS = "stars"
        const val CONDITION_RECENTLY_UPDATED = "updated"

        fun newInstance(sort: String?, onSortChanged: (sort: String?) -> Unit): SortDialog {
            val args = Bundle()
            args.putString(ARG_KEY_SORT, sort)
            val fragment = SortDialog(onSortChanged)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var binding: FragmentDialogFilterBinding

    private var sort: String? = null
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        dialog?.window?.apply {
            setGravity(Gravity.BOTTOM)
            setBackgroundDrawableResource(R.drawable.shape_rect_round_top_corners_14_color_primary)
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setWindowAnimations(R.style.BottomSlidingUpDialogAnimation)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sort = savedInstanceState?.getString(ARG_KEY_SORT)?:arguments?.getString(ARG_KEY_SORT)
        binding = FragmentDialogFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            radioGroup.check(
                when(sort) {
                    CONDITION_MOST_STARS -> {
                        R.id.mostStars
                    }
                    CONDITION_RECENTLY_UPDATED -> {
                        R.id.recentlyUpdated
                    }
                    else -> {
                        R.id.bestMatch
                    }
                }
            )
            radioGroup.setOnCheckedChangeListener { _, i ->
                when(i) {
                    R.id.bestMatch -> {
                        onSortChanged(null)
                    }
                    R.id.mostStars -> {
                        onSortChanged(CONDITION_MOST_STARS)
                    }
                    R.id.recentlyUpdated -> {
                        onSortChanged(CONDITION_RECENTLY_UPDATED)
                    }
                }
                dismiss()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(ARG_KEY_SORT, sort)
    }
}