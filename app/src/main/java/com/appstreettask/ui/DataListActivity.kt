package com.appstreettask.ui

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat.makeSceneTransitionAnimation
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appstreettask.R
import com.appstreettask.dagger.AppComponents
import com.appstreettask.dagger.DaggerAppComponents
import com.appstreettask.data.remote.NetworkUtility
import com.appstreettask.ui.adapter.DataListAdapter
import com.appstreettask.ui.callbacks.ItemClickListener
import com.appstreettask.ui.model.DataDetailsItem
import com.appstreettask.ui.viewmodel.DataViewModel
import com.appstreettask.ui.viewmodel.ViewModelFactory
import javax.inject.Inject

class DataListActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: DataViewModel
    private lateinit var mToolbar: Toolbar
    private lateinit var rvRecyclerView: RecyclerView
    private lateinit var noInternetScreen: LinearLayout
    private lateinit var tryAgainButton: Button
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var githubAdapter: DataListAdapter
    private var mProgressLoader: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.git_dataitem_list)
        requestPermission()
        injectDependency()
        initViews()
        setToolbar()
        setViews()
        initData()
        setListeners()
    }

    private fun setListeners() {
        tryAgainButton.setOnClickListener {
            hideNoInternetScreen()
            getRepositoryListData()
        }
    }

    private fun injectDependency() {

        var components: AppComponents = DaggerAppComponents.builder().build()
        components.injectRepositoryListActivity(this)
        viewModel =
            ViewModelProviders
                .of(this, viewModelFactory)
                .get(DataViewModel::class.java)
    }

    private fun setToolbar() {
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setSupportActionBar(mToolbar)
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        mToolbar.title = getString(R.string.app_title)
    }

    private fun initViews() {
        mToolbar = findViewById(R.id.toolbar)
        rvRecyclerView =
            findViewById(R.id.rv_recycler_view)
        noInternetScreen = findViewById(R.id.no_internet_screen)
        tryAgainButton = findViewById(R.id.try_again)
        mLayoutManager = LinearLayoutManager(this)
        mLayoutManager.orientation = LinearLayoutManager.VERTICAL
        rvRecyclerView.setHasFixedSize(true)
        rvRecyclerView.layoutManager = mLayoutManager
        githubAdapter = DataListAdapter(
            this, onItemClickListener
        )
        rvRecyclerView.adapter = githubAdapter
    }

    val onItemClickListener = object : ItemClickListener {
        override fun onItemClick(item: DataDetailsItem, view: View) {
            startRepoDetailActivity(item, view)
        }
    }

    private fun startRepoDetailActivity(item: DataDetailsItem, view: View) {
        val intent = Intent(this@DataListActivity,
            DataDetailActivity::class.java
        )
        intent.putExtra("data", item)

        val activityOptionsCompat =
            makeSceneTransitionAnimation(
                this@DataListActivity,
                view, getString(R.string.transition_element_name)
            )
        ActivityCompat.startActivity(this@DataListActivity,
            intent, activityOptionsCompat.toBundle()
        )
        overridePendingTransition(0, R.anim.exit_slide_right)
    }

    private fun setViews() {
        rvRecyclerView.visibility = View.VISIBLE
        noInternetScreen.visibility = View.GONE
    }

    private fun initData() {
        getRepositoryListData()
    }

    private fun getRepositoryListData() {
        if (!NetworkUtility.isNetworkAvailable) {
            showNoInternetScreen()
            return
        }
        viewModel.getRepositoryListData()
        viewModel.githubRepositoryList.observe(this, Observer { repoList ->

            githubAdapter.setData(repoList)
            githubAdapter.notifyDataSetChanged()

        })
        viewModel.githubRepositoryError.observe(this, Observer { error ->
        })

        viewModel.loadingState.observe(this, Observer { isLoading ->
            if (isLoading) {
                showLoader()
            } else {
                hideLoader()
            }
        })
    }

    fun showNoInternetScreen() {
        noInternetScreen.visibility = View.VISIBLE
        rvRecyclerView.visibility = View.GONE
    }

    private fun hideNoInternetScreen() {
        rvRecyclerView.visibility = View.VISIBLE
        noInternetScreen.visibility = View.GONE
    }

    fun showLoader() {
        if (mProgressLoader == null) {
            mProgressLoader = ProgressDialog(this)
        }
        mProgressLoader!!.setMessage(getString(R.string.progress_title_list))
        mProgressLoader!!.setTitle(getString(R.string.progress_please_wait))
        mProgressLoader!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        mProgressLoader!!.setCancelable(false)
        mProgressLoader!!.show()
    }

    fun hideLoader() {
        if (mProgressLoader != null) {
            mProgressLoader!!.dismiss()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if (itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private val REQUEST_CODE_ASK_PERMISSIONS = 123

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat
                .requestPermissions(
                    this@DataListActivity,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_CODE_ASK_PERMISSIONS
                )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_ASK_PERMISSIONS -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                Toast.makeText(
                    this@DataListActivity,
                    getString(R.string.permissin_granted),
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                // Permission Denied
                Toast.makeText(this@DataListActivity, getString(R.string.permission_denied), Toast.LENGTH_SHORT)
                    .show()
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}

