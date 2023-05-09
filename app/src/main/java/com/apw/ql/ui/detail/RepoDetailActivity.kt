package com.apw.ql.ui.detail

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.apw.ql.R
import com.apw.ql.databinding.ActivityDetailBinding

class RepoDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_KEY_URL = "com.apw.ql.extra_key_url"
    }

    private lateinit var binding: ActivityDetailBinding
    private var mUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.webView.webChromeClient = object: WebChromeClient() {

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                binding.progressBar.progress = newProgress
                binding.progressBar.visibility = if (newProgress == 100) View.GONE else View.VISIBLE
            }
        }

        mUrl = savedInstanceState?.getString(EXTRA_KEY_URL)?:intent?.extras?.getString(EXTRA_KEY_URL)
        mUrl?.let {
            binding.webView.loadUrl(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_web, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.action_open_with_browser) {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(mUrl))
            try {
                startActivity(browserIntent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, "No app found", Toast.LENGTH_SHORT).show()
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mUrl?.let { outState.putString(EXTRA_KEY_URL, it) }
    }
}