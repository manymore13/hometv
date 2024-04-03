package com.yan.hometv

import android.util.Log
import com.tencent.mmkv.MMKV
import com.yan.source.utils.request
import net.bjoernpetersen.m3u.M3uParser
import net.bjoernpetersen.m3u.model.M3uEntry

class MediaRepository {


    companion object {
        const val TAG = "MediaRepository"
        const val WEEK: Long = 604800000L
        const val tvTimeKey = "tv_cache_time"
        const val tvSourceKey = "tv_cache_source"
    }

    suspend fun getSource(): MutableMap<String, MutableList<M3uEntry>> {
        val mediaMap = mutableMapOf<String, MutableList<M3uEntry>>()
        val m3uEntryList = getTvMediaSource()
        m3uEntryList?.forEach { m3uEntry ->
            val iconUrl = ""
            val mediaSource = ""
            m3uEntry.location
        }
        return mediaMap
    }

    suspend fun getTvMediaSource(): MutableList<M3uEntry>? {
        val kv = MMKV.defaultMMKV()
        val recordTime = kv.decodeLong(tvTimeKey)
        var m3uFileStr: String? = null
        if ((recordTime == 0L) || (System.currentTimeMillis() - recordTime) > WEEK) {
            val url = "https://cdn.jsdelivr.net/gh/fanmingming/live@latest/tv/m3u/ipv6.m3u"
            m3uFileStr = request(url)
            if (m3uFileStr.isNotEmpty()) {
                kv.encode(tvTimeKey, System.currentTimeMillis())
                kv.encode(tvSourceKey, m3uFileStr)
                Log.d(TAG, "---- net get source")
            }
        } else {
            Log.d(TAG, "---- kkv get source")
            m3uFileStr = kv.decodeString(tvSourceKey)
        }
        var m3uEntryList: MutableList<M3uEntry>? = null
        if (m3uFileStr != null) {
            m3uEntryList = M3uParser.parse(m3uFileStr).toMutableList()
        }
        Log.d(TAG, "$m3uEntryList")
        return m3uEntryList
    }
}