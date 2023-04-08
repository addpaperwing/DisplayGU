package com.example.displaygu.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.displaygu.R
import com.example.displaygu.data.Repo
import com.example.displaygu.data.User
import com.example.displaygu.databinding.ItemHeaderBinding
import com.example.displaygu.databinding.ItemRepoBinding

class RepoAdapter (private val onClick: (repo: Repo) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<Repo> = emptyList()
    private var user: User? = null

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            R.layout.item_header
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
            else -> {
                throw RuntimeException("Unknown view type on create view holder")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            holder.bind(user)
        } else if (holder is RepoViewHolder) {
            holder.bind(items[position - 1], onClick)
        }
    }
    override fun getItemCount(): Int = items.size + if (user != null) 1 else 0
    fun updateItems(user: User?, repos: List<Repo>) {
        this.user = user
        items = repos
        notifyDataSetChanged()
    }

    inner class HeaderViewHolder(private val binding: ItemHeaderBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User?) {
            binding.avatarImageView.load(user?.avatarUrl)
            binding.nameTextView.text = user?.name
        }
    }
    inner class RepoViewHolder(private val binding: ItemRepoBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(repo: Repo, onClickBlock: (repo: Repo) -> Unit) {
            binding.nameTextView.text = repo.name
            binding.descTextView.text = repo.description
            binding.root.setOnClickListener {
                onClickBlock(repo)
            }
        }
    }
}