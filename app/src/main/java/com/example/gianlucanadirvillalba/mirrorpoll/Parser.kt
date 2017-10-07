package com.example.gianlucanadirvillalba.mirrorpoll

import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by gianlucanadirvillalba on 02/10/2017.
 */
//TODO manca il controllo sull'inesistenza dei campi (ad esempio optimalnotiesdata)
class Parser
{
    companion object
    {
        fun parseJsonGetPolls(response: JSONArray): HashMap<String, String>
        {
            var jsonObject: JSONObject
            var i = 0
            val map = HashMap<String, String>()
            while (i < response.length())
            {
                jsonObject = response.getJSONObject(i)
                map[jsonObject.getString("pollname")] = jsonObject.getString("pollid")
                i++
            }
            return map
        }

        fun parseJsonGetCandidates(response: JSONArray): ArrayList<String>
        {
            var jsonObject: JSONObject
            val candidates = ArrayList<String>()
            var i = 0
            while (i < response.length())
            {
                jsonObject = response.getJSONObject(i)
                candidates.add(jsonObject.getString("candname").toString())
                i++
            }
            return candidates
        }

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