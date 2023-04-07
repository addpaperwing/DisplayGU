package com.example.displaygu.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.displaygu.data.Repo
import com.example.displaygu.databinding.ItemRepoBinding

class RepoAdapter: RecyclerView.Adapter<RepoAdapter.RepoViewHolder>() {

    private var items: List<Repo> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return RepoViewHolder(ItemRepoBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(repos: List<Repo>) {
        items = repos
        notifyDataSetChanged()
    }

    inner class RepoViewHolder(private val binding: ItemRepoBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(repo: Repo) {
            binding.nameTextView.text = repo.name
            binding.descTextView.text = repo.description
        }
    }
}