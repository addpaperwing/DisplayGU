package com.apw.ql.ui.main

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.apw.ql.R
import com.apw.ql.data.remote.State
import com.apw.ql.databinding.ActivityMainBinding
import com.apw.ql.exts.onScrollToBottom
import com.apw.ql.ui.detail.RepoDetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        private const val SAVE_KEY_SORT = "sort"
        private const val SAVE_KEY_QUERY = "query"
    }

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainViewModel>()

    private var sort: String? = null
    private lateinit var query: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sort = savedInstanceState?.getString(SAVE_KEY_SORT)
        savedInstanceState?.getString(SAVE_KEY_QUERY)?.let { query = it }

        setSupportActionBar(binding.toolbar)

        binding.sortBy.text = getSortOrderText(sort)
        binding.sortBy.setOnClickListener {
            showSortDialog()
        }

        val adapter = RepoAdapter { repo ->
            startActivity(Intent(this, RepoDetailActivity::class.java).apply {
                putExtra(RepoDetailActivity.EXTRA_KEY_URL, repo?.htmlUrl)
            })
        }
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        val dividerItemDecoration = DividerItemDecoration(this, layoutManager.orientation).apply {
            ContextCompat.getDrawable(this@MainActivity, R.drawable.shape_rect_spacing_12dp)?.let {
                setDrawable(it)
            }
        }
        binding.recyclerView.addItemDecoration(dividerItemDecoration)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.onScrollToBottom {
            viewModel.getData(query, sort)
        }

        binding.retryButton.setOnClickListener {
            viewModel.getData(query, sort)
        }

        viewModel.result.observe(this) {
            when(it) {
                is State.Success -> {
                    adapter.update(it.data)
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.retryButton.visibility = View.INVISIBLE
                    binding.errorTextView.visibility = View.INVISIBLE
                    binding.progressBar.visibility = View.INVISIBLE
                }
                is State.Loading -> {
                    binding.recyclerView.visibility = View.INVISIBLE
                    binding.retryButton.visibility = View.INVISIBLE
                    binding.errorTextView.visibility = View.INVISIBLE
                    binding.progressBar.visibility = View.VISIBLE
                }
                is State.Error -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.recyclerView.visibility = View.INVISIBLE
                    binding.retryButton.visibility = View.VISIBLE
                    binding.errorTextView.visibility = View.VISIBLE
                    binding.errorTextView.text = it.getErrorMessage()

                }
            }
            binding.tutorialImage.visibility = View.GONE
            binding.tutorialText.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_search, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        val searchView = (searchItem.actionView as SearchView)
        searchView.apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))

            setOnQueryTextListener(object: SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        this@MainActivity.query = it
                        viewModel.getData(it, sort)
                    }

                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }

        return true
    }

    private fun showSortDialog() {
        SortDialog.newInstance(sort) {
            sort = it
            binding.sortBy.text = getSortOrderText(it)
            if (this::query.isInitialized) viewModel.getData(query, it)
        }.show(supportFragmentManager, null)
    }

    private fun getSortOrderText(sort: String?) = getString(
        R.string.sort_by, getString(when(sort) {
        SortDialog.CONDITION_MOST_STARS -> R.string.most_stars
        SortDialog.CONDITION_RECENTLY_UPDATED -> R.string.recently_updated
        else -> R.string.best_match
    }))

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVE_KEY_SORT, sort)
        outState.putString(SAVE_KEY_QUERY, query)
    }
}