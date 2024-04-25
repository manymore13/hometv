package com.wei.source

import android.content.Context
import android.util.Log
import com.wei.source.db.Channel
import com.wei.source.db.ChannelDao
import com.wei.source.db.ChannelUrl
import com.wei.source.db.DatabaseManager
import com.wei.source.db.Source
import com.wei.source.utils.request
import kotlinx.coroutines.flow.Flow
import net.bjoernpetersen.m3u.M3uParser
import java.io.IOException

class SourceRepository(val context: Context) {

    companion object {
        const val TAG = "SourceRepository"
    }

    private val channelDao: ChannelDao by lazy {
        DatabaseManager.getInstance(context).getChannelDao()
    }

    suspend fun updateSelectChannelFromSource(sourceId: Long, selectChannelId: Long): Int {
        return channelDao.updateSelectChannelFromSource(sourceId, selectChannelId)
    }

    /**
     * 处理源和频道的更新
     */
    @Throws(IOException::class)
    suspend fun manageSourceRefresh(source: Source, refresh: Boolean = true): Long {
        val sourceId: Long = if (source.refreshTime == 0L) {
            // 插入源
            channelDao.insertSource(source.apply {
                refreshTime = System.currentTimeMillis()
            })
        } else {
            source.id
        }
        val countChannel = channelDao.countChannel(sourceId)
        Log.d(TAG, "manageSourceRefresh refresh countChannel = $countChannel $refresh")
        if (countChannel == 0L) {
            refreshSource(source.apply {
                id = sourceId
            })
        }
        return sourceId
    }

    /**
     * 从网上获取数据，刷新源数据
     */
    private suspend fun refreshSource(source: Source) {
        Log.d(TAG, "refresh source: $source")
        val sourceId: Long = source.id
        val m3uStr = request(source.url)
        val m3uEntryList = M3uParser.parse(m3uStr)
        val channelList = mutableListOf<Channel>()
        val channelUrl = mutableListOf<ChannelUrl>()
        val dbChannelCount = channelDao.countChannel(sourceId)
        var oldChannel: Channel? = null
        if (dbChannelCount > 0) {
            val selectChannelId = source.selectChannelId
            oldChannel = channelDao.getChannelById(selectChannelId)
            // 换新数据，本地数据要清掉
            channelDao.deleteChannelBySourceId(sourceId)
        }
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
        if (oldChannel?.name?.isNotEmpty() == true) {
            val newChannel =
                channelDao.getChannelBySourceIdAndChannelName(sourceId, oldChannel.name)
            newChannel?.run {
                val count = channelDao.updateSource(source.apply {
                    selectChannelId = newChannel.id
                })
                Log.d(TAG, "updateSource count = $count")
            }
        }
        ids.forEachIndexed { index, id ->
            channelUrl[index].channelId = id
        }
        channelDao.insertChannelUrl(channelUrl)
    }

    suspend fun getSourceByName(sourceName: String): Source? {
        return channelDao.getSourceByName(sourceName)
    }

    suspend fun getSourceById(sourceId: Long, isLoadDefault: Boolean = false): Source? {
        var source = channelDao.getSourceById(sourceId)
        if (isLoadDefault && source == null) {
            val sourceList = channelDao.getAllSource()
            if (sourceList.isNotEmpty()) {
                source = sourceList[0]
            }
        }
        return source
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

    suspend fun deleteChannelBySourceId(sourceId: Long): Int {
        return channelDao.deleteChannelBySourceId(sourceId)
    }

    suspend fun getChannelUrlByChannelId(channelId: Long): MutableList<ChannelUrl> {
        return channelDao.getChannelUrls(channelId)
    }

    suspend fun getChannelById(channelId: Long): Channel? {
        return channelDao.getChannelById(channelId)
    }

    suspend fun getChannelByName(channelName: String): Channel? {
        return channelDao.getChannelByName(channelName)
    }

    suspend fun getAllChannels(sourId: Long): MutableList<Channel> {
        return channelDao.getChannelBySourceId(sourId)
    }

    suspend fun getAllChannelsFlow(sourId: Long): Flow<MutableList<Channel>> {
        return channelDao.getChannelFlowBySourceId(sourId)
    }

}