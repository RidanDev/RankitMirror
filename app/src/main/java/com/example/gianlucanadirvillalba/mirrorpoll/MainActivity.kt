package com.example.gianlucanadirvillalba.mirrorpoll

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.FloatingActionButton
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.estimote.coresdk.common.config.EstimoteSDK
import com.estimote.coresdk.common.requirements.SystemRequirementsChecker

//TODO mostrare i candidati sulle card direttamente ordinati con il pattern fornito
class MainActivity : AppCompatActivity()
{
    private lateinit var mToolbar: android.support.v7.widget.Toolbar
    private lateinit var mAdapter: RecyclerAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mLinearLayoutManager: LinearLayoutManager
    private lateinit var mSwipeRefreshLayout: android.support.v4.widget.SwipeRefreshLayout
    private lateinit var mFloatingActionButton: FloatingActionButton
    private lateinit var mSendButton: Button
    private lateinit var mLayoutInflater : LayoutInflater
    //private var mCardView: CardView? = null
    private var twice = false

    companion object
    {
        private val APP_ID: String = "mirror-poll-1de"
        private val APP_TOKEN: String = "8edcf6fe47305e1f61714a4dbb037951"
        lateinit var mProgressBar: View

        fun onStopProgress()
        {
            mProgressBar.visibility = View.GONE
        }
    }


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity2)

//        MirrorClient.Builder(MyApplication.appContext)
//                .useMirrorWithIds("7ad83a1b886ae2e6321319c38c48fd11")
//                .setDebugModeEnabled(true)
//                .setRepeatableDisplayRequests(true)
//                .build()

        EstimoteSDK.initialize(this, APP_ID, APP_TOKEN)
        EstimoteSDK.enableDebugLogging(true)
        setUpUI()
        setUpRecyclerView()
        if (savedInstanceState == null)
        {
            RequestAPI.getPolls(mAdapter)
            //TaskLoadPoll(mAdapter).execute()
        } else onStopProgress() //blocca animazione rotazione(?)
    }

    private fun setUpRecyclerView()
    {
        mAdapter = RecyclerAdapter(this)
        mLinearLayoutManager = LinearLayoutManager(this)
        mRecyclerView.layoutManager = mLinearLayoutManager as RecyclerView.LayoutManager?
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
        mFloatingActionButton.setOnClickListener {
            val intent = packageManager.getLaunchIntentForPackage("sapienza.informatica.rankit")
            startActivity(intent)
        }


//        mSendButton.setOnClickListener {
//            Log.d(MyApplication.LOG, "onSend")
//        }


    }

    private fun setUpUI()
    {
        //mLayoutInflater = layoutInflater
        mToolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false) //non mostrare tasto back
        mProgressBar = findViewById(R.id.loadingPanel)
        mRecyclerView = findViewById(R.id.poll_list) as RecyclerView
        //mCardView = (findViewById(R.id.card_view) as CardView?)
        mFloatingActionButton = findViewById(R.id.fab) as FloatingActionButton
        //var buttonView = layoutInflater.inflate(R.layout.custom_list_poll, null)
        //mSendButton = buttonView.findViewById(R.id.send_button) as Button

        //mSendButton = findViewById(R.id.send_button) as Button?


    }

    override fun onResume()
    {
        super.onResume()
        SystemRequirementsChecker.checkWithDefaultDialogs(this)
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
