package com.example.displaygu.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.displaygu.R
import com.example.displaygu.data.Repo
import com.example.displaygu.databinding.FragmentItemCardBinding

private const val KEY_ARGS_REPO = "com.example.displaygu.key_args_repo"
class ItemCardFragment : DialogFragment() {

    companion object {

        fun newInstance(repo: Repo): ItemCardFragment {
            val args = Bundle()
            args.putParcelable(KEY_ARGS_REPO, repo)
            val fragment = ItemCardFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var binding: FragmentItemCardBinding

    private var repo: Repo? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentItemCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(KEY_ARGS_REPO, Repo::class.java)
        } else {
            arguments?.getParcelable(KEY_ARGS_REPO)
        }

        binding.nameTextView.text = repo?.name
        binding.lastUpdatedTextView.text = getString(R.string.last_updated, repo?.updateAt)
        binding.starTextView.text = getString(R.string.star_, (repo?.stargazersCount?:0))
        binding.forkTextView.text = getString(R.string.fork_, (repo?.forks?:0))
        binding.descTextView.text = repo?.description
    }
}