package com.yan.source.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException


var client = OkHttpClient()

@Throws(IOException::class)
suspend fun request(url: String) = withContext(Dispatchers.IO) {
    val request: Request = Request.Builder()
        .url(url)
        .build()
    client.newCall(request).execute().use { response -> return@use response.body!!.string() }
}