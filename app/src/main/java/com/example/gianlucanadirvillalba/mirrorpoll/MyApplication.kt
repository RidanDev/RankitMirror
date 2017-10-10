package com.example.gianlucanadirvillalba.mirrorpoll

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.estimote.sdk.mirror.context.DisplayCallback
import com.estimote.sdk.mirror.context.MirrorContextManager
import com.estimote.sdk.mirror.context.Zone
import com.estimote.sdk.mirror.core.common.exception.MirrorException
import com.estimote.sdk.mirror.core.connection.Dictionary
import com.estimote.sdk.mirror.core.connection.MirrorDevice
import org.json.JSONObject

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

        private lateinit var ctxMr: MirrorContextManager

        fun sendToMirror(poll: Poll)
        {
            ctxMr = MirrorContextManager.createMirrorContextManager(this.appContext)
            val dictionary = Dictionary()
            dictionary.template = "pollsapienza"
            dictionary.put("pollname", poll.name)
            dictionary.put("pollid", poll.id)
            for (i in poll.candidates.indices) dictionary.put("candidate${i + 1}", poll.candidates[i])

            ctxMr.display(dictionary, Zone.WHEREVER_YOU_ARE, object : DisplayCallback
            {
                override fun onFinish()
                {
                    Log.d(MyApplication.LOG, "onFinish")
                }

                override fun onFailure(exception: MirrorException?)
                {
                    Log.d(MyApplication.LOG, "onFailure")
                    Toast.makeText(MyApplication.appContext, "onFailure: $exception", Toast.LENGTH_SHORT).show()
                }

                override fun onData(data: JSONObject?)
                {
                    Log.d(MyApplication.LOG, "onData $data")
                }

                override fun onDataDisplayed(mirrorDevice: MirrorDevice?)
                {
                    Log.d(MyApplication.LOG, "onDataDisplayed")
                    Toast.makeText(MyApplication.appContext, "Data successfully received from Mirror", Toast.LENGTH_SHORT).show()
                }

            })

        }
    }


}
