package com.yan.hometv

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tencent.mmkv.MMKV
import com.yan.db.Source
import com.yan.hometv.bean.MediaItem
import com.yan.hometv.utils.toast
import com.yan.source.MediaSource
import com.yan.source.SourceHelper
import com.yan.source.setDefault
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MediaViewModel(application: Application) : AndroidViewModel(application) {

    val complete = MutableLiveData<String>()
    val mediaList = mutableListOf<MediaItem>()
    val selectMediaLiveData = MutableLiveData<MediaItem>()
    val showLoading = MutableLiveData(false)
    private val sourceHelper = SourceHelper(application)
    private var currentItem = 0

    private val mediaRepo: MediaRepository by lazy {
        MediaRepository()
    }

    companion object {
        const val TAG = "MediaViewModel"
        const val RECENT_MEDIA = "RECENT_MEDIA"
        const val DEFAULT_SOURCE = "default_source_name"
        const val DEFAULT_SOURCE_URL =
            "https://cdn.jsdelivr.net/gh/fanmingming/live@latest/tv/m3u/ipv6.m3u"
        const val DEFAULT_SOURCE_NAME = "电视源"

    }

    fun init() = viewModelScope.launch {
        val url = "https://cdn.jsdelivr.net/gh/fanmingming/live@latest/tv/m3u/ipv6.m3u"
        sourceHelper.initSource(Source("fanmingming", url))
    }

    fun loadSource(sourceName: String) = viewModelScope.launch {
        try {
            initSource(MediaSource(sourceName))
        } catch (e: Exception) {
            showLoading.value = false
            toast(e.message)
        }
    }

    fun addNewSource(mediaSource: MediaSource) = viewModelScope.launch {
        try {
            initSource(mediaSource)
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

    fun selectMediaItem(position: Int) {
        currentItem = if (checkPosition(position)) {
            position
        } else {
            0
        }
        if (checkPosition(currentItem)) {
            selectMediaLiveData.value = mediaList[currentItem]
            val kv = MMKV.defaultMMKV()
            kv.encode(RECENT_MEDIA, mediaList[currentItem])
        }

    }

    fun next() {
        selectMediaItem(currentItem + 1)
    }

    fun prev() {
        selectMediaItem(currentItem - 1)
    }

    private suspend fun initSource(mediaSource: MediaSource) = withContext(Dispatchers.IO) {
        showLoading.postValue(true)
        val mapSource = mediaRepo.getMediaSource(mediaSource)
//        val oldLogoHost = "https://live.fanmingming.com/"
//        val newLogoHost = "https://cdn.jsdelivr.net/gh/fanmingming/live@latest/"
        val remoteSource = mapSource?.map { m3uEntry ->
            MediaItem(
                m3uEntry.title ?: "",
                m3uEntry.location.toString(),
                m3uEntry.metadata["tvg-logo"] ?: ""
//                m3uEntry.metadata["tvg-logo"]?.replace(oldLogoHost, newLogoHost) ?: ""
            )
        }?.toMutableList()
        mediaSource.setDefault()
        mediaList.clear()
        mediaList.addAll(remoteSource ?: mutableListOf())
        complete.postValue("complete")
        showLoading.postValue(false)
    }

}