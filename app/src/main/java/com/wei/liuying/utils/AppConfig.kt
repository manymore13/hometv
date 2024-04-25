package com.wei.liuying.utils

import com.wei.liuying.MediaViewModel
import com.wei.source.utils.kv

object AppConfig {

    fun getSelectedSourceId(): Long {
        return kv.getLong(SELECTED_SOURCE_ID, 1)
    }

    fun setSelectedSourceId(sourceId: Long) {
        kv.encode(SELECTED_SOURCE_ID, sourceId)
    }

    /**
     * 获取更新周期
     */
    fun getSourceUpdateCycleTime(): Long {
//        return kv.decodeString(SOURCE_UPDATE_TIME_KEY, "1")!!.toLong()
        return 1L
    }

    fun setSourceUpdateCycleTime(time: Long) {
        kv.encode(SOURCE_UPDATE_TIME_KEY, time.toString())
    }

    fun setRecentChannelName(channelName: String) {
        kv.encode(MediaViewModel.RECENT_MEDIA_NAME, channelName)
    }

    fun getRecentChannelName(): String {
        return kv.getString(MediaViewModel.RECENT_MEDIA_NAME, "")!!
    }

}