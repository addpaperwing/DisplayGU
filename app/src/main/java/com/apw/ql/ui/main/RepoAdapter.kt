package com.apw.ql.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apw.ql.R
import com.apw.ql.data.model.Repo
import com.apw.ql.databinding.ItemLoadingBinding
import com.apw.ql.databinding.ItemRepoBinding
import com.apw.ql.toDefaultTime

class RepoAdapter (private val onClick: (repo: Repo?) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: ArrayList<Repo> = ArrayList()

    override fun getItemViewType(position: Int): Int {
        return if (position == items.size) {
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
            is RepoViewHolder -> {
                holder.bind(items[position], onClick)
            }
        }
    }
    override fun getItemCount(): Int = items.size + (if (items.isEmpty()) 0 else 1) //Footer

    fun update(list: List<Repo>) {
        //We have a footer, last item of list is (item.size - 1), lastItemPosition (footer position) is item.size
        //So is the insertSize, we need to insert a list and a footer
        val lastItemPosition = items.size
        val insertSize = list.size + 1
        items.addAll(list)
        notifyItemRangeChanged(lastItemPosition, insertSize)
    }

    inner class RepoViewHolder(private val binding: ItemRepoBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(repo: Repo, onClickBlock: (repo: Repo) -> Unit) {
            binding.nameTextView.text = repo.name
            binding.descTextView.visibility = if (repo.description == null) View.GONE else View.VISIBLE
            binding.descTextView.text = repo.description

            val starInInfo =
                if (repo.stargazersCount > 0) {
                    itemView.context.resources.getQuantityString(R.plurals.star, repo.stargazersCount, repo.stargazersCount)
                } else {
                    ""
                }
            val updateInfo = itemView.context.getString(R.string.update_on, repo.updateAt.toDefaultTime())

            binding.infoTextView.text = "${repo.language?:""}${if (repo.language != null && starInInfo.isNotBlank()) " · " else ""}$starInInfo"
            binding.updateTextView.text = updateInfo
            binding.root.setOnClickListener {
                onClickBlock(repo)
            }
        }
    }

    inner class LoadingViewHolder(binding: ItemLoadingBinding): RecyclerView.ViewHolder(binding.root)
}