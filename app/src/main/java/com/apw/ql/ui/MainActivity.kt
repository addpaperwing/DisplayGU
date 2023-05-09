package com.apw.ql.ui

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.apw.ql.R
import com.apw.ql.data.Repo
import com.apw.ql.databinding.ActivityMainBinding
import com.apw.ql.network.Async
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainViewModel>()

    private var mSort: String? = null
    private var refresh: Boolean = false
    private var mQuery = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = RepoAdapter { clickOn, repo ->
            when(clickOn) {
                RepoAdapter.ON_CLICK_HEADER -> {
                    showSortDialog()
                }
                RepoAdapter.ON_CLICK_ITEM -> {
                    repo?.let {
                        ItemCardDialogFragment.newInstance(it).show(supportFragmentManager, null)
                    }
                }
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

        viewModel.result.observe(this) {
            adapter.update(it, mSort, refresh)
            binding.recyclerView.visibility = View.VISIBLE
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
                        mQuery = it
                        viewModel.getData(it, null)
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
            viewModel.getData(mQuery, it)
        }.show(supportFragmentManager, "sort")
    }
}