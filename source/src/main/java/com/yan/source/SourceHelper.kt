package com.yan.source

import android.content.Context
import com.yan.db.Channel
import com.yan.db.ChannelDao
import com.yan.db.DatabaseManager
import com.yan.db.Source
import net.bjoernpetersen.m3u.M3uParser

class SourceHelper(val context: Context) {

    private val channelDao: ChannelDao by lazy {
        DatabaseManager.getInstance(context).getChannelDao()
    }

    private suspend fun flushSource(sourceId: Long) {
        val source = channelDao.getSourceById(sourceId) ?: return
        val m3uStr = request(source.url)
        val m3uEntryList = M3uParser.parse(m3uStr)
        val channelList = mutableListOf<Channel>()
        m3uEntryList.forEach {
            val iconUrl = it.metadata["tvg-logo"]
            val groupTitle = it.metadata["group-title"] ?: ""
            val channelName = it.title ?: ""
            if (channelName.isBlank()) {
                return@forEach
            }
            channelList.add(
                Channel(channelName, iconUrl, sourceId, groupTitle)
            )
        }
        channelDao.insertChannels(channelList)
    }

    suspend fun initSource(source: Source) {
        val dbSource = channelDao.getSourceByName(sourceName = source.name)
        var sourceId = 0L
        if (dbSource != null) {
            // 存在源
            sourceId = dbSource.id

        } else {
            // 不存在源
            sourceId = channelDao.insertSource(source)
        }
        flushSource(sourceId)
    }

}