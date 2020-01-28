package com.appstreettask.ui

import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.webkit.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.appstreettask.R
import com.appstreettask.data.remote.NetworkUtility
import com.appstreettask.ui.model.DataDetailsItem
import android.widget.ImageView as ImageView1

class DataDetailActivity : AppCompatActivity() {
    private lateinit var repoData: DataDetailsItem
    private lateinit var mToolbar: Toolbar
    private lateinit var userImage: ImageView1
    private lateinit var userName: TextView
    private lateinit var repoName: TextView
    private lateinit var repoDescription: TextView
    private lateinit var githWebview: WebView
    private var mProgressLoader: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository_detail)
        chechInternetConnection()
        initViews()
        setToolbar()
        getIntentData()
        setData()
        loadWebView()
    }

    private fun chechInternetConnection() {
        if (!NetworkUtility.isNetworkAvailable) {
            Toast.makeText(this, getString(R.string.no_internet_msg), Toast.LENGTH_SHORT).show()
            onBackPressed()
        }
    }

    private fun getIntentData() {
        repoData = intent.getParcelableExtra("data")
        if (repoData == null) {
            Toast.makeText(this, getString(R.string.somthing_wrong_msg), Toast.LENGTH_SHORT).show()
            onBackPressed()
        }
    }

    private fun setToolbar() {
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setSupportActionBar(mToolbar)
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

    private fun initViews() {
        mToolbar = findViewById(R.id.toolbar)
        userImage = findViewById(R.id.user_image)
        userName = findViewById(R.id.user_name)
        repoName = findViewById(R.id.repo_name)
        repoDescription = findViewById(R.id.repo_description)
        githWebview = findViewById(R.id.wv_repo)
    }

    private fun setData() {
        mToolbar.title = repoData.name
        userName.text = getString(R.string.user) + " "+ repoData.username
        repoName.text = getString(R.string.repo)+ " " + repoData.repo.name
        repoDescription.text = getString(R.string.description) + " "+ repoData.repo.description
        AppStreetApplication.getImageLoader().DisplayImage(repoData.avatar, userImage)
    }

    private fun loadWebView() {
        githWebview.settings.javaScriptEnabled = true
        githWebview.settings.domStorageEnabled = true
        githWebview.settings.loadsImagesAutomatically = true
        githWebview.settings.builtInZoomControls = true
        githWebview.webChromeClient = WebChromeClient()
        githWebview.webViewClient = WebViewClients()
        githWebview.loadUrl(repoData.repo.url)
    }

    inner class WebViewClients : WebViewClient() {
        init {
            showLoader()
        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            hideLoader()
        }

        override fun onReceivedError(
            view: WebView,
            request: WebResourceRequest,
            error: WebResourceError
        ) {
            super.onReceivedError(view, request, error)
            hideLoader()
            Toast.makeText(
                this@DataDetailActivity,
                getString(R.string.loading_error), Toast.LENGTH_SHORT
            ).show()
            finish()
        }
    }

    private fun showLoader() {
        if (mProgressLoader == null) {
            mProgressLoader = ProgressDialog(this)
        }
        mProgressLoader!!.setMessage(getString(R.string.progree_title_detail))
        mProgressLoader!!.setTitle(getString(R.string.progress_please_wait))
        mProgressLoader!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        mProgressLoader!!.setCancelable(false)
        mProgressLoader!!.show()
    }

    private fun hideLoader() {
        if (mProgressLoader != null) {
            mProgressLoader!!.dismiss()
        }
    }

    override fun onBackPressed() {
        ActivityCompat.finishAfterTransition(this)
        super.onBackPressed()
        overridePendingTransition(0, R.anim.exit_slide_left)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if (itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
