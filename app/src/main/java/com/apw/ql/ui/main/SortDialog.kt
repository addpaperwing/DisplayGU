package com.apw.ql.ui.main

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.DialogFragment
import com.apw.ql.R
import com.apw.ql.data.model.SortDialogItem
import com.apw.ql.databinding.FragmentDialogFilterBinding
import com.apw.ql.ui.compose.SortingGroup

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

//    private var sort: String? = null


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
//        sort =
        binding = FragmentDialogFilterBinding.inflate(inflater, container, false).apply {
            composeView.setContent {
                var sort by rememberSaveable {
                    mutableStateOf(arguments?.getString(ARG_KEY_SORT))
                }
                SortingGroup(list = listOf(
                    SortDialogItem(
                        0,
                        stringResource(id = R.string.most_stars),
                        sort == CONDITION_MOST_STARS
                    ),
                    SortDialogItem(
                        1,
                        stringResource(id = R.string.recently_updated),
                        sort == CONDITION_RECENTLY_UPDATED
                    ),
                    SortDialogItem(
                        2,
                        stringResource(id = R.string.best_match),
                        sort == null
                    ),
                ), onSelected = {
                    sort = when (it.id) {
                        0 -> {
                            CONDITION_MOST_STARS
                        }

                        1 -> {
                            CONDITION_RECENTLY_UPDATED
                        }

                        else -> {
                            null
                        }
                    }
                    onSortChanged(sort)
                })
            }
        }
        return binding.root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        binding.run {
//            radioGroup.check(
//                when(sort) {
//                    CONDITION_MOST_STARS -> {
//                        R.id.mostStars
//                    }
//                    CONDITION_RECENTLY_UPDATED -> {
//                        R.id.recentlyUpdated
//                    }
//                    else -> {
//                        R.id.bestMatch
//                    }
//                }
//            )
//            radioGroup.setOnCheckedChangeListener { _, i ->
//                when(i) {
//                    R.id.bestMatch -> {
//                        onSortChanged(null)
//                    }
//                    R.id.mostStars -> {
//                        onSortChanged(CONDITION_MOST_STARS)
//                    }
//                    R.id.recentlyUpdated -> {
//                        onSortChanged(CONDITION_RECENTLY_UPDATED)
//                    }
//                }
//                dismiss()
//            }
//        }
//    }
//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        outState.putString(ARG_KEY_SORT, sort)
//    }
}