package com.example.gianlucanadirvillalba.mirrorpoll

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
        lateinit var poll: Poll
        var pollArray = ArrayList<Poll>()

        fun parseJsonGetPolls(response: JSONArray, adapter: RecyclerAdapter)
        {
            //Log.d(MyApplication.LOG, "parseJsonGetPolls")
            var jsonObject: JSONObject
            var i = 0
            while (i < response.length())
            {
                jsonObject = response.getJSONObject(i)
                poll = Poll()
                poll.name = jsonObject.getString("pollname")
                poll.id = jsonObject.getString("pollid")
                poll.votes = jsonObject.getString("votes")
                pollArray.add(poll)
                i++
            }
            for (p in pollArray) RequestAPI.getCandidates(p, adapter)
        }

        fun parseJsonGetCandidates(response: JSONArray, poll: Poll, adapter: RecyclerAdapter)
        {
            var jsonObject: JSONObject
            var i = 0
            while (i < response.length())
            {
                jsonObject = response.getJSONObject(i)
                poll.candidates.add(jsonObject.getString("candname").toString())
                i++
            }
            //Log.d(MyApplication.LOG, "name:${poll.name} id:${poll.id} votes:${poll.votes} candidates:${poll.candidates}")
            adapter.onAddPoll(pollArray)
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