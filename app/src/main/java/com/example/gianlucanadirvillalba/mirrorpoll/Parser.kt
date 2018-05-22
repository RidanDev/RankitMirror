package com.example.gianlucanadirvillalba.mirrorpoll

import android.util.Log
import android.view.View
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by gianlucanadirvillalba on 02/10/2017.
 */
//TODO mancano i controlli sull'inesistenza dei campi (ad esempio optimalnotiesdata)
class Parser
{
    companion object
    {
        var success = JSONArray()
        lateinit var poll: Poll
        var pollArray = ArrayList<Poll>()

        fun parseJsonGetPolls(response: JSONArray, adapter: RecyclerAdapter)
        {
            Log.d(MyApplication.LOG, "parseJsonGetPolls")
            var jsonObject: JSONObject
            var i = 0
            while (i < response.length())
            {
                jsonObject = response.getJSONObject(i)
                poll = Poll()
                if (jsonObject.getString("pollname").trim().isNotEmpty() && jsonObject.getString("pollname").isNotEmpty())
                {
                    poll.name = jsonObject.getString("pollname")
                    poll.id = jsonObject.getString("pollid")
                    poll.votes = jsonObject.getString("votes")
                    poll.image = jsonObject.getString("pollimage")
                    poll.deadline = jsonObject.getString("deadline")
                    poll.updated = jsonObject.getString("updated")
                    pollArray.add(poll)
                }
                i++
            }
            for (p in pollArray) TaskLoadPoll(adapter, p).execute()
            //adapter.onAddPoll(pollArray)
            //Log.d(MyApplication.LOG, "LOAD FINISHED")
            MainActivity.mProgressBar.visibility = View.GONE
            //TaskLoadPoll2(adapter, pollArray);
        }

        fun parseJsonGetCandidates(response: JSONArray, poll: Poll, adapter: RecyclerAdapter)
        {
            Log.d(MyApplication.LOG, "parseJsonGetCandidates")
            var jsonObject: JSONObject
            var i = 0

            while (i < response.length())
            {
                jsonObject = response.getJSONObject(i)
                poll.candidates.add("${jsonObject.getString("candname")}\n")
                i++
            }
            adapter.addNewData(adapter, poll)
        }

        //TODO mi serve un metodo per estrarre l'ordine dei candidati dal pattern (javascript o android?)
        fun parseJsonGetPattern(response: JSONObject): String
        {
            val jsonObject: JSONObject
            val array = response.getJSONArray("optimalnotiesdata")
            var pattern = ""
            val i = 0
            while (i < array.length())
            {
                jsonObject = array.getJSONObject(i)
                pattern = jsonObject.getString("pattern")
                break
            }
            return pattern
        }
    }
}