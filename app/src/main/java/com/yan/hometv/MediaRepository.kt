package com.yan.hometv

import android.util.Log
import com.tencent.mmkv.MMKV
import com.yan.source.utils.MediaSource
import com.yan.source.utils.request
import com.yan.source.utils.save
import com.yan.source.utils.setDefault
import net.bjoernpetersen.m3u.M3uParser
import net.bjoernpetersen.m3u.model.M3uEntry

class MediaRepository {


    companion object {
        const val TAG = "MediaRepository"
        const val WEEK: Long = 604800000L
        const val tvTimeKey = "tv_cache_time"
        const val tvSourceKey = "tv_cache_source"
    }

//    suspend fun getSource(): MutableMap<String, MutableList<M3uEntry>> {
//        val mediaMap = mutableMapOf<String, MutableList<M3uEntry>>()
//        val sourceUrl = "https://cdn.jsdelivr.net/gh/fanmingming/live@latest/tv/m3u/ipv6.m3u"
//        val m3uEntryList = getMediaSource(sourceUrl)
//        m3uEntryList?.forEach { m3uEntry ->
//            val iconUrl = ""
//            val mediaSource = ""
//            m3uEntry.location
//        }
//        return mediaMap
//    }

    suspend fun getMediaSource(source: MediaSource): MutableList<M3uEntry>? {

        val recordTime = source.recordTime
        var m3uFileStr: String? = null
        if ((recordTime == 0L) || (System.currentTimeMillis() - recordTime) > WEEK) {
            m3uFileStr = request(source.sourceUrl)
            if (m3uFileStr.isNotEmpty()) {
                source.save(System.currentTimeMillis(), m3uFileStr)
                source.setDefault()
                Log.d(TAG, "---- net get source")
            }
        } else {
            Log.d(TAG, "---- kkv get source")
            m3uFileStr = source.sourceContent
        }
        var m3uEntryList: MutableList<M3uEntry>? = null
        if (m3uFileStr != null) {
            m3uEntryList = M3uParser.parse(m3uFileStr).toMutableList()
        }
        Log.d(TAG, "$m3uEntryList")
        return m3uEntryList
    }
}