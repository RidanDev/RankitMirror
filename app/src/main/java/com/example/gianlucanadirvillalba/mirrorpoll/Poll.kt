package com.example.gianlucanadirvillalba.mirrorpoll

/**
 * Created by gianlucanadirvillalba on 06/10/2017.
 */
class Poll
{
    lateinit var name : String
    lateinit var id : String
    lateinit var votes : String
    lateinit var pattern : String
    lateinit var image : String
    var candidates = ArrayList<String>()

    override fun toString(): String = "id: $id, name: $name, votes: $votes, image: $image"

}