package com.example.displaygu.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.displaygu.R
import com.example.displaygu.data.Repo
import com.example.displaygu.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchButton.setOnClickListener {
            val userName = binding.userNameEditText.text.toString()
            viewModel.getData(userName)
        }

        val adapter = RepoAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        viewModel.result.observe(this) {
            binding.avatarImageView.load(it.first.avatarUrl)
            binding.nameTextView.text = it.first.name
            adapter.updateItems(it.second)
        }
    }
}