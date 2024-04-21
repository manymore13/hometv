package com.wei.liuying.utils

import com.wei.liuying.MediaViewModel
import com.wei.source.utils.kv

object AppConfig {

    fun getSelectedSourceId(): Long {
        return kv.getLong(SELECTED_SOURCE_ID, 0)
    }

    fun setSelectedSourceId(sourceId: Long) {
        kv.encode(SELECTED_SOURCE_ID, sourceId)
    }

    fun getSourceUpdateCycleTime(): Long {
        return kv.decodeString(SOURCE_UPDATE_TIME_KEY, "86400000")!!.toLong()
    }

    fun setSourceUpdateCycleTime(time: Long) {
        kv.encode(SOURCE_UPDATE_TIME_KEY, time.toString())
    }

    fun setRecentChannel(channelId: Long) {
        kv.encode(MediaViewModel.RECENT_MEDIA, channelId)
    }

    fun getRecentChannel(): Long {
        return kv.getLong(MediaViewModel.RECENT_MEDIA, 0)
    }

}