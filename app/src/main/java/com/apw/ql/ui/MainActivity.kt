package com.apw.ql.ui

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.apw.ql.R
import com.apw.ql.databinding.ActivityMainBinding
import com.apw.ql.data.remote.State
import com.apw.ql.onScrollToBottom
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainViewModel>()

    private var mSort: String? = null
    private lateinit var mQuery: String

    private var refresh: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.sortBy.text = getString(R.string.sort_by, mSort?: getString(R.string.best_match))
        binding.sortBy.setOnClickListener {
            showSortDialog()
        }

        val adapter = RepoAdapter { repo ->
            repo?.let {
                ItemCardDialogFragment.newInstance(it).show(supportFragmentManager, null)
            }
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
            viewModel.getData(mQuery, mSort, false)
        }

        binding.retry.setOnClickListener {
            viewModel.getData(mQuery, mSort, true)
        }

        viewModel.result.observe(this) {
            when(it) {
                is State.Success -> {
//                    if (refresh) binding.recyclerView.recycledViewPool.clear()
                    adapter.update(it.data, refresh)
                    refresh = false
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.retry.visibility = View.INVISIBLE
                    binding.error.visibility = View.INVISIBLE
                    binding.progressBar.visibility = View.INVISIBLE
                }
                is State.Loading -> {
                    if (refresh) {
                        binding.recyclerView.visibility = View.INVISIBLE
                        binding.retry.visibility = View.INVISIBLE
                        binding.error.visibility = View.INVISIBLE
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
                is State.Error -> {
                    if (refresh) {
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.recyclerView.visibility = View.INVISIBLE
                        binding.retry.visibility = View.VISIBLE
                        binding.error.visibility = View.VISIBLE
                        binding.error.text = it.getErrorMessage()
                    } else {
                        Toast.makeText(this, it.getErrorMessage(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            binding.tutorialImage.visibility = View.GONE
            binding.tutorialText.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_search, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = (searchItem.actionView as SearchView)
        searchView.apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))

            setOnQueryTextListener(object: OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        refresh = true
                        mQuery = it
                        viewModel.getData(it, mSort, refresh)
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
        SortDialog.newInstance(mSort) {
            refresh = mSort != it
            mSort = it
            viewModel.getData(mQuery, it, refresh)
        }.show(supportFragmentManager, "sort")
    }
}