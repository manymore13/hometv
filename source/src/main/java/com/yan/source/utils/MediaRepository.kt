package com.yan.source.utils

import net.bjoernpetersen.m3u.M3uParser
import net.bjoernpetersen.m3u.model.M3uEntry

class MediaRepository {


    companion object {
        const val TAG = "MediaRepository"
    }

    suspend fun getSource(): MutableMap<String, MutableList<M3uEntry>> {
        val mediaMap = mutableMapOf<String, MutableList<M3uEntry>>()
        val m3uEntryList = getMediaSource()
        m3uEntryList.forEach { m3uEntry ->
            val iconUrl = ""
            val mediaSource = ""
            m3uEntry.location
        }
        return mediaMap
    }

    suspend fun getMediaSource(): MutableList<M3uEntry> {
        val url = "https://cdn.jsdelivr.net/gh/fanmingming/live@latest/tv/m3u/ipv6.m3u"
        val m3uFile = request(url)
        val m3uEntryList = M3uParser.parse(m3uFile)
        println("$m3uFile")
        return m3uEntryList.toMutableList()
    }
}