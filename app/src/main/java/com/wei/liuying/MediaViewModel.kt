package com.wei.liuying

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tencent.mmkv.MMKV
import com.wei.source.db.Source
import com.wei.liuying.bean.MediaItem
import com.wei.liuying.bean.toMediaItem
import com.wei.liuying.utils.SELECTED_SOURCE_NAME
import com.wei.liuying.utils.SOURCE_UPDATE_TIME_KEY
import com.wei.liuying.utils.getAppSharedPreferences
import com.wei.liuying.utils.isNeedRefreshData
import com.wei.liuying.utils.toast
import com.wei.source.SourceHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MediaViewModel(application: Application) : AndroidViewModel(application) {

    private val pref by lazy { getAppSharedPreferences() }
    val complete = MutableLiveData<String>()
    val mediaList = mutableListOf<MediaItem>()
    val selectMediaLiveData = MutableLiveData<MediaItem>()
    val showLoading = MutableLiveData(false)
    private val sourceHelper = SourceHelper(application)
    private var currentItem = 0

    companion object {
        const val TAG = "MediaViewModel"
        const val RECENT_MEDIA = "RECENT_MEDIA"
        const val DEFAULT_SOURCE_NAME = "默认源"
        const val DEFAULT_SOURCE_URL =
            "https://cdn.jsdelivr.net/gh/fanmingming/live@latest/tv/m3u/ipv6.m3u"

    }

    fun initSource() = viewModelScope.launch {

        val selectedSourceName = pref.getString(SELECTED_SOURCE_NAME, DEFAULT_SOURCE_NAME)!!
        val selectedSource = sourceHelper.getSourceByName(selectedSourceName) ?: Source(
            selectedSourceName, DEFAULT_SOURCE_URL, 0
        )

        loadChannels(selectedSource)
    }

    fun getAllSourceFlow(): Flow<MutableList<Source>> {
        return sourceHelper.getAllSourceFlow()
    }

    suspend fun getAllSource(): MutableList<Source> {
        return sourceHelper.getAllSource()
    }

    suspend fun getMediaItemByChannelId(channelId: Long):MediaItem? {
        var url = ""
        val urls = sourceHelper.getChannelUrlByChannelId(channelId)
        if (urls.isNotEmpty()) {
            url = urls[0].url
        }
        return sourceHelper.getChannelById(channelId)?.toMediaItem()?.apply {
            mediaUrl = url
        }
    }

    fun deleteSources(sourceList: MutableList<Source>) = viewModelScope.launch {
        sourceHelper.deleteSources(sourceList)
        toast(App.getContext().getString(R.string.del_success))
    }


    fun loadChannels(source: Source) = viewModelScope.launch {
        showLoading.postValue(true)

        val updateTime = pref.getLong(SOURCE_UPDATE_TIME_KEY, 86400000L)
        val isNeedRefresh = isNeedRefreshData(source.refreshTime, updateTime)
        sourceHelper.manageSourceRefresh(source, isNeedRefresh)
        Log.d(TAG, "loadChannels: $source")

        val sourceId: Long = source.id
        val channels = sourceHelper.getAllChannels(sourceId)
        val mediaItems = channels.map {
            it.toMediaItem()
        }

        mediaList.clear()
        mediaList.addAll(mediaItems)
        complete.postValue("complete")
        showLoading.postValue(false)
    }

    fun loadChannels(id: Long) = viewModelScope.launch {
        val source = sourceHelper.getSourceById(id)
        if (source != null) {
            loadChannels(source)
        }
    }

    fun addNewSource(source: Source) = viewModelScope.launch {
        try {
            loadChannels(source)
        } catch (e: Exception) {
            e.printStackTrace()
            showLoading.value = false
            toast(e.message)
        }
    }

    private fun checkPosition(position: Int): Boolean {
        return (position < (mediaList.size - 1)) && (position >= 0)
    }

    fun getRecentMediaItem(): MediaItem? {
        val kv = MMKV.defaultMMKV()
        return kv.decodeParcelable(RECENT_MEDIA, MediaItem::class.java)
    }

    fun selectMediaItem(position: Int) = viewModelScope.launch {
        currentItem = if (checkPosition(position)) {
            position
        } else {
            0
        }
        if (checkPosition(currentItem)) {
            val curMedia = mediaList[currentItem]
            val channelUrls = sourceHelper.getChannelUrlByChannelId(curMedia.id)
            curMedia.mediaUrl = channelUrls[0].url
            selectMediaLiveData.value = curMedia
            val kv = MMKV.defaultMMKV()
            kv.encode(RECENT_MEDIA, curMedia.id)
        }
    }

    fun next() {
        selectMediaItem(currentItem + 1)
    }

    fun prev() {
        selectMediaItem(currentItem - 1)
    }

//    private suspend fun initSource(mediaSource: MediaSource) = withContext(Dispatchers.IO) {
//        showLoading.postValue(true)
//        val mapSource = mediaRepo.getMediaSource(mediaSource)
////        val oldLogoHost = "https://live.fanmingming.com/"
////        val newLogoHost = "https://cdn.jsdelivr.net/gh/fanmingming/live@latest/"
//        val remoteSource = mapSource?.map { m3uEntry ->
//            MediaItem(
//                m3uEntry.title ?: "",
//                m3uEntry.location.toString(),
//                m3uEntry.metadata["tvg-logo"] ?: ""
////                m3uEntry.metadata["tvg-logo"]?.replace(oldLogoHost, newLogoHost) ?: ""
//            )
//        }?.toMutableList()
//        mediaSource.setDefault()
//        mediaList.clear()
//        mediaList.addAll(remoteSource ?: mutableListOf())
//        complete.postValue("complete")
//        showLoading.postValue(false)
//    }

}