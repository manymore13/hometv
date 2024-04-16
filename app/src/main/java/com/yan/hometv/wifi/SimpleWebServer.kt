package com.yan.hometv.wifi

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import fi.iki.elonen.NanoHTTPD

/**
 * @author manymore13
 * @Description SimpleWebServer
 * @date 2024/4/8
 */
class SimpleWebServer(
    port: Int,
    val serverReceiver: ((sourceName: String, sourceNet: String) -> Unit)
) : NanoHTTPD(port), DefaultLifecycleObserver {


    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        start()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        stop()
    }

    override fun serve(session: IHTTPSession): Response {
        val uri = session.uri
        return if (uri.startsWith("/source")) {
            val params = session.parameters
            val sourceName = params["name"]?.get(0) ?: "sourceName"
            val sourceNet = params["url"]?.get(0) ?: "sourceNet"
            serverReceiver(sourceName, sourceNet)
            newFixedLengthResponse("Message received: $sourceName");
        } else {
            newFixedLengthResponse(
                Response.Status.NOT_FOUND,
                NanoHTTPD.MIME_PLAINTEXT,
                "Not found"
            )
        }
    }
}