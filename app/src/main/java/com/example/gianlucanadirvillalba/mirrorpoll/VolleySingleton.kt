package com.example.gianlucanadirvillalba.mirrorpoll

import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

/**
 * Created by gianlucanadirvillalba on 29/09/2017.
 */

class VolleySingleton private constructor()
{
    val requestQueue: RequestQueue

    init
    {
        requestQueue = Volley.newRequestQueue(MyApplication.appContext)
    }

    companion object
    {
        private var sInstance: VolleySingleton? = null

        val instance: VolleySingleton
            get()
            {
                if (sInstance == null) sInstance = VolleySingleton()
                return sInstance as VolleySingleton
            }
    }


}
