package com.example.gianlucanadirvillalba.mirrorpoll

import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by gianlucanadirvillalba on 07/10/2017.
 */
class RequestAPI
{
    companion object
    {
        private lateinit var mRequestQueue: RequestQueue

        fun getPolls(adapter: RecyclerAdapter)
        {
            Log.d(MyApplication.LOG, "getPolls")
            mRequestQueue = VolleySingleton.instance.requestQueue
            val request = JsonArrayRequest(Request.Method.GET,
                    UrlEndPoints.URL_RANKIT + UrlEndPoints.GET_POLLS, null, Response.Listener<JSONArray>
            { response ->
                Toast.makeText(MyApplication.appContext, "Response received from: ${UrlEndPoints.URL_RANKIT + UrlEndPoints.GET_POLLS}", Toast.LENGTH_SHORT).show()
                MyApplication.success = true
                MainActivity.onStopProgress()
                Parser.parseJsonGetPolls(response, adapter)
                Log.d(MyApplication.LOG, "getPolls: $response")

            }, Response.ErrorListener
            { error ->
                Toast.makeText(MyApplication.appContext, error.toString(), Toast.LENGTH_LONG).show()
                MyApplication.success = true
                //MainActivity.onStopProgress()
            })
            mRequestQueue.add(request)
        }

        fun getCandidates(poll: Poll, adapter: RecyclerAdapter)
        {

            mRequestQueue = VolleySingleton.instance.requestQueue
            val request = JsonArrayRequest(Request.Method.GET,
                    UrlEndPoints.URL_RANKIT + UrlEndPoints.GET_CANDIDATES + UrlEndPoints.URL_CHAR_QUESTION + UrlEndPoints.POLL_ID + poll.id,
                    null, Response.Listener<JSONArray>
            { response ->

                if (response.length() != 0) Parser.parseJsonGetCandidates(response, poll, adapter)
            }, Response.ErrorListener
            { error ->
                Toast.makeText(MyApplication.appContext, error.toString(), Toast.LENGTH_LONG).show()
            })
            mRequestQueue.add(request)
        }


        fun getPatter(pollid: String)
        {
            mRequestQueue = VolleySingleton.instance.requestQueue
            val request = JsonObjectRequest(Request.Method.GET,
                    UrlEndPoints.URL_RANKIT + UrlEndPoints.GET_RESULTS + UrlEndPoints.URL_CHAR_QUESTION + UrlEndPoints.POLL_ID + pollid,
                    null, Response.Listener<JSONObject>
            { response ->
                Parser.parseJsonGetPattern(response)

            }, Response.ErrorListener
            { error ->
                Toast.makeText(MyApplication.appContext, error.toString(), Toast.LENGTH_LONG).show()
            })
            mRequestQueue.add(request)
        }
    }
}