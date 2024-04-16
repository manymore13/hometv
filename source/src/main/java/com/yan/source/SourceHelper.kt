package com.yan.source

import android.content.Context
import android.util.Log
import com.yan.source.db.Channel
import com.yan.source.db.ChannelDao
import com.yan.source.db.ChannelUrl
import com.yan.source.db.DatabaseManager
import com.yan.source.db.Source
import com.yan.source.utils.request
import kotlinx.coroutines.flow.Flow
import net.bjoernpetersen.m3u.M3uParser

class SourceHelper(val context: Context) {

    private val channelDao: ChannelDao by lazy {
        DatabaseManager.getInstance(context).getChannelDao()
    }

    private suspend fun refreshSource(source: Source) {
        Log.d("SourceHelper", "refresh source: $source")
        val sourceId: Long = source.id
        val m3uStr = request(source.url)
        val m3uEntryList = M3uParser.parse(m3uStr)
        val channelList = mutableListOf<Channel>()
        val channelUrl = mutableListOf<ChannelUrl>()
        m3uEntryList.forEach {
            val iconUrl = it.metadata["tvg-logo"]
            val groupTitle = it.metadata["group-title"] ?: ""
            val channelName = it.title ?: ""
            val url = it.location
            if (channelName.isBlank() || url.toString().isBlank()) {
                return@forEach
            }
            channelUrl.add(
                ChannelUrl(0, url.toString())
            )
            channelList.add(
                Channel(channelName, iconUrl, sourceId, groupTitle)
            )
        }
        val ids = channelDao.insertChannels(channelList)
        ids.forEachIndexed { index, id ->
            channelUrl[index].channelId = id
        }
        channelDao.insertChannelUrl(channelUrl)
    }

    suspend fun getSourceByName(sourceName: String): Source? {
        return channelDao.getSourceByName(sourceName)
    }

    suspend fun getSourceById(sourceId: Long): Source? {
        return channelDao.getSourceById(sourceId)
    }

    fun getAllSourceFlow(): Flow<MutableList<Source>> {
        return channelDao.getAllSourceFlow()
    }

    suspend fun getAllSource(): MutableList<Source> {
        return channelDao.getAllSource()
    }
    suspend fun deleteSources(sourceList: MutableList<Source>): Int {
        return channelDao.deleteSources(sourceList)
    }

    suspend fun getChannelUrlByChannelId(channelId: Long): MutableList<ChannelUrl> {
        return channelDao.getChannelUrls(channelId)
    }

    /**
     * 处理源和频道的更新
     */
    suspend fun manageSourceRefresh(source: Source, refresh: Boolean = true) {
        val sourceId: Long = if (source.refreshTime == 0L) {
            channelDao.insertSource(source.apply {
                refreshTime = System.currentTimeMillis()
            })
        } else {
            source.id
        }
        val countChannel = channelDao.countChannel(sourceId)
        if (countChannel == 0L || refresh) {
            refreshSource(source.apply {
                id = sourceId
            })
        }
    }

    suspend fun getAllChannels(sourId: Long): MutableList<Channel> {
        return channelDao.getChannelBySourceId(sourId)
    }

}