package com.wei.liuying

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.wei.source.db.Source
import com.wei.liuying.bean.MediaItem
import com.wei.liuying.bean.toMediaItem
import com.wei.liuying.utils.AppConfig
import com.wei.liuying.utils.isNeedRefreshData
import com.wei.liuying.utils.toast
import com.wei.source.SourceRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MediaViewModel(application: Application) : AndroidViewModel(application) {

    val complete = MutableLiveData<String>()
    val mediaItemList = mutableListOf<MediaItem>()
    val selectMediaLiveData = MutableLiveData<MediaItem>()
    val showLoading = MutableLiveData(false)
    private var selectSourceJob: Job? = null
    private var selectChannelJob: Job? = null
    private val sourceRepository = SourceRepository(application)
    private var currentItem = 0

    companion object {
        const val TAG = "MediaViewModel"
        const val RECENT_MEDIA = "RECENT_MEDIA"
        const val DEFAULT_SOURCE_NAME = "默认源"

        const val ACTION_CHANGE_SOURCE = "ACTION_CHANGE_SOURCE"

        const val DEFAULT_SOURCE_URL =
            "https://cdn.jsdelivr.net/gh/fanmingming/live@latest/tv/m3u/ipv6.m3u"

    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // 处理广播
            when (intent.action) {
                ACTION_CHANGE_SOURCE -> {
                    val sourceId = AppConfig.getSelectedSourceId()
                    selectSource(sourceId)
                }
            }
        }
    }

    fun initSource() = viewModelScope.launch {
        LocalBroadcastManager.getInstance(App.getContext()).registerReceiver(
            broadcastReceiver,
            IntentFilter(ACTION_CHANGE_SOURCE)
        )
        val selectedSourceId = AppConfig.getSelectedSourceId()

        var selectedSource: Source? = sourceRepository.getSourceById(sourceId = selectedSourceId)

        if (selectedSource == null) {
            val sourceList = sourceRepository.getAllSource()
            if (sourceList.isNotEmpty()) {
                selectedSource = sourceList[0]
            }
        }

        if (selectedSource == null) {
            selectedSource = Source(0, DEFAULT_SOURCE_NAME, DEFAULT_SOURCE_URL)
            Log.d(TAG, "加载默认source $DEFAULT_SOURCE_NAME")
        }

        selectSource(selectedSource)
    }

    fun getAllSourceFlow(): Flow<MutableList<Source>> {
        return sourceRepository.getAllSourceFlow()
    }

    suspend fun getAllSource(): MutableList<Source> {
        return sourceRepository.getAllSource()
    }

    suspend fun getMediaItemByChannelId(channelId: Long): MediaItem? {
        var url = ""
        val urls = sourceRepository.getChannelUrlByChannelId(channelId)
        if (urls.isNotEmpty()) {
            url = urls[0].url
        }
        return sourceRepository.getChannelById(channelId)?.toMediaItem()?.apply {
            mediaUrl = url
        }
    }

    fun deleteSources(sourceList: MutableList<Source>) = viewModelScope.launch {
        sourceRepository.deleteSources(sourceList)
        toast(App.getContext().getString(R.string.del_success))
    }


    private fun selectSource(source: Source) {

        selectSourceJob?.cancel()
        selectSourceJob = viewModelScope.launch {

            showLoading.postValue(true)

            val updateTime = AppConfig.getSourceUpdateCycleTime()
            val isNeedRefresh = isNeedRefreshData(source.refreshTime, updateTime)
            val sourceId = sourceRepository.manageSourceRefresh(source, isNeedRefresh)

            AppConfig.setSelectedSourceId(sourceId = sourceId)
            Log.d(TAG, "isNeedRefresh = $isNeedRefresh loadChannels: $source")

            sourceRepository.getAllChannelsFlow(sourceId).collect { channels ->
                val mediaItems = channels.map {
                    it.toMediaItem()
                }

                mediaItemList.clear()
                mediaItemList.addAll(mediaItems)
                updateCurrentItemIndex()
                complete.postValue("complete")
                showLoading.postValue(false)
            }
        }
    }

    private fun updateCurrentItemIndex() {
        val channelId = AppConfig.getRecentChannel()
        mediaItemList.forEachIndexed { index, mediaItem ->
            if (mediaItem.id == channelId) {
                currentItem = index
                return@forEachIndexed
            }
        }
    }

    fun selectSource(id: Long) = viewModelScope.launch {
        val source = sourceRepository.getSourceById(id)
        if (source != null) {
            try {
                selectSource(source)
            } catch (e: Exception) {
                e.printStackTrace()
                toast(e.message)
            }
        }
    }

    /**
     * 添加新源
     */
    fun addNewSource(source: Source) = viewModelScope.launch {
        try {
            selectSource(source)
        } catch (e: Exception) {
            e.printStackTrace()
            toast(e.message)
        }
    }

    private fun checkPosition(position: Int): Boolean {
        if (mediaItemList.isEmpty()) {
            return false
        }
        return (position < (mediaItemList.size - 1)) && (position >= 0)
    }

    suspend fun getRecentChannel(): MediaItem? {
        val channelId = AppConfig.getRecentChannel()
        val channel = sourceRepository.getChannelById(channelId)
        return channel?.toMediaItem()?.apply {
            val url = getUrl(channelId)
            if(url?.isNotEmpty() == true){
                mediaUrl = url
            }
        }
    }

    fun selectChannel(position: Int) {
        selectChannelJob?.cancel()
        selectChannelJob = viewModelScope.launch {
            if (checkPosition(position)) {
                val curMedia = mediaItemList[position]
                currentItem = position
                val channelUrls = sourceRepository.getChannelUrlByChannelId(curMedia.id)
                if (channelUrls.isNotEmpty()) {
                    curMedia.mediaUrl = channelUrls[0].url
                    selectMediaLiveData.value = curMedia
                    AppConfig.setRecentChannel(curMedia.id)
                }
            }
        }
    }

    suspend fun getUrl(channelId: Long): String? {
        val channelUrls = sourceRepository.getChannelUrlByChannelId(channelId)
        return if (channelUrls.isNotEmpty()) {
            channelUrls[0].url
        } else {
            null
        }
    }

    /**
     * 下一个频道
     */
    fun nextMediaItem() {
        selectChannel(currentItem + 1)
    }

    /**
     * 上一个频道
     */
    fun prevMediaItem() {
        selectChannel(currentItem - 1)
    }

    override fun onCleared() {
        super.onCleared()
        LocalBroadcastManager.getInstance(App.getContext()).unregisterReceiver(broadcastReceiver)
    }

}