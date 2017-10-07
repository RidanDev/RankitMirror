package com.example.gianlucanadirvillalba.mirrorpoll

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.estimote.coresdk.common.config.EstimoteSDK
import com.estimote.coresdk.common.requirements.SystemRequirementsChecker
import com.estimote.sdk.mirror.context.DisplayCallback
import com.estimote.sdk.mirror.context.MirrorContextManager
import com.estimote.sdk.mirror.context.Zone
import com.estimote.sdk.mirror.core.common.exception.MirrorException
import com.estimote.sdk.mirror.core.connection.Dictionary
import com.estimote.sdk.mirror.core.connection.MirrorDevice
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity()
{
    private lateinit var mRequestQueue: RequestQueue
    private lateinit var mSendButton: Button
    private lateinit var mEditPoll: EditText
    private var pollmap = HashMap<String, String>()
    private lateinit var pollid: String
    private lateinit var pollname: String
    private lateinit var ctxMr: MirrorContextManager
    private val APP_ID: String = "mirror-poll-1de"
    private val APP_TOKEN: String = "8edcf6fe47305e1f61714a4dbb037951"
    //private val MIRROR_ID: String = "7ad83a1b886ae2e6321319c38c48fd11"
    private var candidates = ArrayList<String>()
    private lateinit var pattern: String
    private lateinit var input: String
    private var twice = false

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        EstimoteSDK.initialize(this, APP_ID, APP_TOKEN)
        EstimoteSDK.enableDebugLogging(true)
        setUpUI()
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

    private fun getCandidates()
    {
        mRequestQueue = VolleySingleton.instance.requestQueue
        val request = JsonArrayRequest(Request.Method.GET,
                UrlEndPoints.URL_RANKIT + UrlEndPoints.GET_CANDIDATES + UrlEndPoints.URL_CHAR_QUESTION + UrlEndPoints.POLL_ID + pollid,
                null, Response.Listener<JSONArray>
        { response ->
            candidates = Parser.parseJsonGetCandidates(response)
            getPattern()
        }, Response.ErrorListener
        { error ->
            Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
        })
        mRequestQueue.add(request)
    }

    private fun getPattern()
    {
        mRequestQueue = VolleySingleton.instance.requestQueue
        val request = JsonObjectRequest(Request.Method.GET,
                UrlEndPoints.URL_RANKIT + UrlEndPoints.GET_RESULTS + UrlEndPoints.URL_CHAR_QUESTION + UrlEndPoints.POLL_ID + pollid,
                null, Response.Listener<JSONObject>
        { response ->
            pattern = Parser.parseJsonGetPattern(response)
            setUpMirror()

        }, Response.ErrorListener
        { error ->
            Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
        })
        mRequestQueue.add(request)
    }

    private fun setUpUI()
    {
        mEditPoll = findViewById(R.id.edit_pollname) as EditText
        mSendButton = findViewById(R.id.button_invia) as Button
        mSendButton.setOnClickListener {
            input = mEditPoll.text.toString()
            if (input.isEmpty())
                Toast.makeText(this, "No input found", Toast.LENGTH_SHORT).show()
            else setUpGetPollsRequest()
        }
    }

    //TODO le richieste vanno fatte in un thread secondario
    private fun setUpGetPollsRequest()
    {
        mRequestQueue = VolleySingleton.instance.requestQueue
        val request = JsonArrayRequest(Request.Method.GET,
                UrlEndPoints.URL_RANKIT + UrlEndPoints.GET_POLLS, null, Response.Listener<JSONArray>
        { response ->
            Toast.makeText(this, "Response received from: ${UrlEndPoints.URL_RANKIT + UrlEndPoints.GET_POLLS}", Toast.LENGTH_SHORT).show()
            pollmap = Parser.parseJsonGetPolls(response)
            for ((k, v) in pollmap)
            {
                if (input == k)
                {
                    pollname = k
                    pollid = v
                    getCandidates()
                    break
                } else if (!pollmap.containsKey(input))
                {
                    Toast.makeText(this, "$input Not Found!", Toast.LENGTH_SHORT).show()
                    break
                }
            }
        }, Response.ErrorListener
        { error ->
            Toast.makeText(MyApplication.appContext, error.toString(), Toast.LENGTH_LONG).show()
        })
        mRequestQueue.add(request)
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
