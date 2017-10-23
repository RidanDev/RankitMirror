package com.example.gianlucanadirvillalba.mirrorpoll

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.Toast
import com.estimote.coresdk.common.config.EstimoteSDK

class MainActivity : AppCompatActivity()
{
    private lateinit var mToolbar: android.support.v7.widget.Toolbar
    private lateinit var mAdapter: RecyclerAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mLinearLayoutManager: LinearLayoutManager
    private lateinit var mSwipeRefreshLayout: android.support.v4.widget.SwipeRefreshLayout
    private var mCardView : CardView? = null
    private var twice = false

    companion object
    {
        private val APP_ID: String = "mirror-poll-1de"
        private val APP_TOKEN: String = "8edcf6fe47305e1f61714a4dbb037951"
        private lateinit var mProgressBar: View

        fun onStopProgress()
        {
            mProgressBar.visibility = View.GONE
        }
    }


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity2)
        EstimoteSDK.initialize(this, APP_ID, APP_TOKEN)
        EstimoteSDK.enableDebugLogging(true)
        setUpUI()
        setUpRecyclerView()
        if (savedInstanceState == null)
        {
            RequestAPI.getPolls(mAdapter)
            //TaskLoadPoll(mAdapter).execute()
        } else onStopProgress()
    }

    private fun setUpRecyclerView()
    {
        mAdapter = RecyclerAdapter(this)
        mLinearLayoutManager = LinearLayoutManager(this)
        mRecyclerView.layoutManager = mLinearLayoutManager
        mRecyclerView.adapter = mAdapter
        mRecyclerView.isNestedScrollingEnabled = false //permette lo scroll fluido
        //mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout) as SwipeRefreshLayout
        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout) as SwipeRefreshLayout
        mSwipeRefreshLayout.setOnRefreshListener {
            Parser.pollArray.clear()
            RecyclerAdapter.data.clear()
            RequestAPI.getPolls(mAdapter)
            if (MyApplication.success)
            {
                mSwipeRefreshLayout.isRefreshing = false
                MyApplication.success = false
            }
        }

    }

    private fun setUpUI()
    {
        //mToolbar = findViewById(R.id.app_bar) as android.support.v7.widget.Toolbar
        mToolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false) //non mostrare tasto back
        mProgressBar = findViewById(R.id.loadingPanel)
        mRecyclerView = findViewById(R.id.poll_list) as RecyclerView
        mCardView = (findViewById(R.id.card_view) as CardView?)
        //mCardView?.cardElevation  = 100F
    }

    override fun onResume()
    {
        super.onResume()
        //SystemRequirementsChecker.checkWithDefaultDialogs(this)
    }

    override fun onBackPressed()
    {
        Log.d(MyApplication.LOG, "back")

        if (twice)
        {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
            System.exit(0)
        }

        Toast.makeText(this, "Please press back again to exit", Toast.LENGTH_SHORT).show()
        Handler().postDelayed({
            twice = false
            Log.d(MyApplication.LOG, "twice: $twice")
        }, 3000)
        twice = true
    }

    override fun onSaveInstanceState(outState: Bundle?)
    {
        super.onSaveInstanceState(outState)
    }
}
