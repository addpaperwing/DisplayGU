package com.example.displaygu.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.displaygu.databinding.ActivityMainBinding
import com.example.displaygu.network.Status
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

            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }

        val adapter = RepoAdapter {
            ItemCardDialogFragment.newInstance(it).show(supportFragmentManager, null)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        viewModel.result.observe(this) {
            adapter.updateItems(it.first, it.second)
        }

        viewModel.taskState.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.INVISIBLE
                }
                Status.LOADING -> {
                    binding.recyclerView.visibility = View.INVISIBLE
                    binding.progressBar.visibility = View.VISIBLE
                }
                Status.FAILED -> {
                    binding.recyclerView.visibility = View.INVISIBLE
                    binding.progressBar.visibility = View.INVISIBLE
                    Toast.makeText(this, it.getErrorMessage(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}