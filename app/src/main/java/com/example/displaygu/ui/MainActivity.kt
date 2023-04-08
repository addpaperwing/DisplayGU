package com.example.displaygu.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.displaygu.R
import com.example.displaygu.data.Repo
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
            ItemCardFragment.newInstance(it).show(supportFragmentManager, null)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        viewModel.result.observe(this) {
            adapter.updateItems(it.first, it.second)
        }

        viewModel.taskState.observe(this) {
            if (it.status == Status.FAILED) {
                adapter.updateItems(null, emptyList())
                Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}