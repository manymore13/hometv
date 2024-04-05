package com.yan.hometv

import android.util.Log
import com.yan.source.utils.MediaSource
import com.yan.source.utils.request
import com.yan.source.utils.save
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

    /**
     * 是否需要从网络上拉取资源
     */
    private fun isNeedFromNet(source: MediaSource): Boolean {
        val recordTime = source.recordTime
        return (recordTime == 0L) || (System.currentTimeMillis() - recordTime) > WEEK
    }

    suspend fun getMediaSource(source: MediaSource): MutableList<M3uEntry>? {

        var m3uFileStr: String? = null
        if (isNeedFromNet(source)) {
            m3uFileStr = request(source.sourceUrl)
            if (m3uFileStr.isNotEmpty()) {
                source.save(System.currentTimeMillis(), m3uFileStr)
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