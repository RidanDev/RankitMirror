package com.example.gianlucanadirvillalba.mirrorpoll

import android.app.Application
import android.content.Context

/**
 * Created by gianlucanadirvillalba on 30/09/2017.
 */

class MyApplication : Application()
{
    override fun onCreate()
    {
        super.onCreate()
        instance = this
    }

    companion object
    {
        var instance: MyApplication? = null
            private set

        val appContext: Context
            get() = instance!!.applicationContext
        val LOG: String = "log"
    }

}
