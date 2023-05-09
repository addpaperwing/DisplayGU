package com.apw.ql.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.apw.ql.R
import com.apw.ql.data.Repo
import com.apw.ql.data.User
import com.apw.ql.databinding.ItemErrorBinding
import com.apw.ql.databinding.ItemHeaderBinding
import com.apw.ql.databinding.ItemLoadingBinding
import com.apw.ql.databinding.ItemRepoBinding
import com.apw.ql.network.Async

class RepoAdapter (private val onClick: (clickOn:Int, repo: Repo?) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val ON_CLICK_HEADER = 0
        const val ON_CLICK_ITEM = 1
        const val ON_CLICK_RETRY = 2
    }

    private var items: ArrayList<Repo> = ArrayList()
    private var errMsg: String? = null
    private var sort: String? = null
    override fun getItemViewType(position: Int): Int {
        return if (items.isEmpty()) {
            if (errMsg == null) {
                R.layout.item_loading
            } else {
                R.layout.item_error
            }
        } else if (position == 0) {
            R.layout.item_header
        } else if (position == items.size + 1) {
            R.layout.item_loading
        } else {
            R.layout.item_repo
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.item_repo -> {
                RepoViewHolder(ItemRepoBinding.inflate(layoutInflater, parent, false))
            }
            R.layout.item_header -> {
                HeaderViewHolder(ItemHeaderBinding.inflate(layoutInflater, parent,false))
            }
            R.layout.item_error -> {
                ErrorViewHolder(ItemErrorBinding.inflate(layoutInflater, parent, false))
            }
            R.layout.item_loading -> {
                LoadingViewHolder(ItemLoadingBinding.inflate(layoutInflater, parent, false))
            }
            else -> {
                throw RuntimeException("Unknown view type on create view holder")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                holder.bind(onClick)
            }
            is RepoViewHolder -> {
                holder.bind(items[position - 1], onClick)
            }
            is ErrorViewHolder -> {
                holder.bind(onClick)
            }
        }
    }
    override fun getItemCount(): Int = items.size +
            //Header
            (if (items.isEmpty()) 0 else 1) +
            //Footer, Error and Loading view
            1
    fun update(response: Async<List<Repo>>, sort: String?, refresh: Boolean = false) {
        when(response) {
            is Async.Success -> {
                if (refresh) items.clear()
                val lastItemPosition = items.size
                //If it's the start of the list, we need to insert a header
                val insertSize = response.data.size + if (lastItemPosition == 0) 1 else 0
                this.sort = sort
                items.addAll(response.data)
                notifyItemRangeChanged(lastItemPosition, insertSize)
            }
            is Async.Error -> {
                errMsg = response.getErrorMessage()
                notifyItemChanged(0)
            }
            is Async.Loading -> {
                notifyItemChanged(0)
            }
        }

    }

    inner class HeaderViewHolder(private val binding: ItemHeaderBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(onClickBlock: (clickOn: Int, repo: Repo?) -> Unit) {
            binding.sortBy.text = itemView.context.getString(R.string.sort_by, sort?: itemView.context.getString(R.string.best_match))
            binding.sortBy.setOnClickListener {
                onClickBlock(ON_CLICK_HEADER, null)
            }
        }
    }
    inner class RepoViewHolder(private val binding: ItemRepoBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(repo: Repo, onClickBlock: (clickOn: Int, repo: Repo) -> Unit) {
            binding.nameTextView.text = repo.name
            binding.descTextView.text = repo.description
            binding.root.setOnClickListener {
                onClickBlock(ON_CLICK_ITEM, repo)
            }
        }
    }

    inner class ErrorViewHolder(private val binding: ItemErrorBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(onClickBlock: (clickOn: Int, repo: Repo?) -> Unit) {
            binding.error.text = errMsg
            binding.retry.setOnClickListener {
                onClickBlock(ON_CLICK_RETRY, null)
            }
        }
    }

    inner class LoadingViewHolder(binding: ItemLoadingBinding): RecyclerView.ViewHolder(binding.root)
}