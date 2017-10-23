package com.example.gianlucanadirvillalba.mirrorpoll

import android.os.AsyncTask
import android.util.Log
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.RequestFuture
import org.json.JSONArray
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit

/**
 * Created by gianlucanadirvillalba on 13/10/2017.
 */
class TaskLoadPoll(a: RecyclerAdapter, p: Poll) : AsyncTask<Unit, Unit, JSONArray>()
{

    private lateinit var mRequestQueue: RequestQueue
    var poll = p
    var adapter = a

    override fun doInBackground(vararg p0: Unit?): JSONArray
    {

        mRequestQueue = VolleySingleton.instance.requestQueue
        val future = RequestFuture.newFuture<JSONArray>()
        val request = JsonArrayRequest(UrlEndPoints.URL_RANKIT + UrlEndPoints.GET_CANDIDATES + UrlEndPoints.URL_CHAR_QUESTION + UrlEndPoints.POLL_ID + poll.id,
                future, future)
        mRequestQueue.add(request)
        try
        {
            //var response = future.get(2, TimeUnit.SECONDS)
            //Log.d(MyApplication.LOG, "response ${response.length()}")

        } catch (e: InterruptedException)
        {
            Log.d(MyApplication.LOG, e.printStackTrace().toString())

        } catch (e: ExecutionException)
        {
            Log.d(MyApplication.LOG, e.printStackTrace().toString())

        }
        return future.get(5, TimeUnit.SECONDS)
    }

    override fun onPostExecute(result: JSONArray?)
    {
        super.onPostExecute(result)
        if (result!!.length() != 0) Parser.parseJsonGetCandidates(result, poll, adapter)
    }

}