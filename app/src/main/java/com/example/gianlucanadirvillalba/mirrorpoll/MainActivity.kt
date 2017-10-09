package com.example.gianlucanadirvillalba.mirrorpoll

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Toast
import com.estimote.coresdk.common.config.EstimoteSDK
import com.estimote.coresdk.common.requirements.SystemRequirementsChecker
import com.estimote.sdk.mirror.context.DisplayCallback
import com.estimote.sdk.mirror.context.MirrorContextManager
import com.estimote.sdk.mirror.context.Zone
import com.estimote.sdk.mirror.core.common.exception.MirrorException
import com.estimote.sdk.mirror.core.connection.Dictionary
import com.estimote.sdk.mirror.core.connection.MirrorDevice
import org.json.JSONObject

class MainActivity : AppCompatActivity()
{
    private lateinit var pollid: String
    private lateinit var pollname: String
    private lateinit var ctxMr: MirrorContextManager
    private val APP_ID: String = "mirror-poll-1de"
    private val APP_TOKEN: String = "8edcf6fe47305e1f61714a4dbb037951"
    //private val MIRROR_ID: String = "7ad83a1b886ae2e6321319c38c48fd11"
    private var candidates = ArrayList<String>()
    private lateinit var pattern: String
    private var twice = false
    private lateinit var mToolbar: android.support.v7.widget.Toolbar
    private lateinit var mAdapter: RecyclerAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mLinearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity2)
        EstimoteSDK.initialize(this, APP_ID, APP_TOKEN)
        EstimoteSDK.enableDebugLogging(true)
        setUpUI()
        setUpRecyclerView()
        RequestAPI.getPolls(mAdapter)
    }

    //TODO aggiungere swype to refresh per aggiornare i dati della recycler view
    private fun setUpRecyclerView()
    {
        mAdapter = RecyclerAdapter(this)
        mRecyclerView = findViewById(R.id.poll_list) as RecyclerView
        mLinearLayoutManager = LinearLayoutManager(this)
        mRecyclerView.layoutManager = mLinearLayoutManager
        mRecyclerView.adapter = mAdapter
    }

    private fun setUpMirror()
    {
        Log.d("log", "SET UP MIRROR")
        Log.d("log", "pollname: $pollname")
        Log.d("log", "pollid: $pollid")
        Log.d("log", "pattern: $pattern")
        ctxMr = MirrorContextManager.createMirrorContextManager(this)
        val dictionary = Dictionary()
        dictionary.template = "pollsapienza"
        dictionary.put("pollname", pollname)
        dictionary.put("pollid", pollid)
        dictionary.put("pattern", pattern)
        for (i in candidates.indices)
        {
            dictionary.put("candidate${i + 1}", candidates[i])
            Log.d("log", "candidate${i + 1}: " + candidates[i])
        }


        ctxMr.display(dictionary, Zone.WHEREVER_YOU_ARE, object : DisplayCallback
        {
            override fun onDataDisplayed(mirrorDevice: MirrorDevice?)
            {
                Log.d("log", "onDataDisplayed")
                Toast.makeText(MyApplication.appContext, "Data successfully received from Mirror", Toast.LENGTH_SHORT).show()
            }

            override fun onData(data: JSONObject?)
            {
                Log.d("log", "onData $data")
            }

            override fun onFinish()
            {
                Log.d("log", "onFinish")
            }

            override fun onFailure(exception: MirrorException?)
            {
                Log.d("log", "onFailure")
                Toast.makeText(MyApplication.appContext, "onFailure: $exception", Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun setUpUI()
    {
        mToolbar = findViewById(R.id.app_bar) as android.support.v7.widget.Toolbar
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
}
