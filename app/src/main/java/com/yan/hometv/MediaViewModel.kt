package com.yan.hometv

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tencent.mmkv.MMKV
import com.yan.hometv.bean.MediaItem
import com.yan.source.utils.MediaSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MediaViewModel(application: Application) : AndroidViewModel(application) {

    val complete = MutableLiveData<String>()
    val mediaList = mutableListOf<MediaItem>()
    val selectMediaLiveData = MutableLiveData<MediaItem>()

    private val mediaRepo: MediaRepository by lazy {
        MediaRepository()
    }

    companion object {
        const val TAG = "MediaViewModel"
        const val DEFAULT_SOURCE = "default_source_name"
        const val DEFAULT_SOURCE_URL =
            "https://cdn.jsdelivr.net/gh/fanmingming/live@latest/tv/m3u/ipv6.m3u"
        const val DEFAULT_SOURCE_NAME = "电视源"

    }

    fun init() = viewModelScope.launch {
        val defaultSourceName = MediaSource.getDefaultSourceName()
        val defaultSource: MediaSource
        if (defaultSourceName.isEmpty()) {
            defaultSource = MediaSource(DEFAULT_SOURCE_NAME, DEFAULT_SOURCE_URL)
        } else {
            defaultSource = MediaSource(defaultSourceName)
        }
        initSource(defaultSource)
    }

    fun addNewSource(mediaSource: MediaSource) = viewModelScope.launch {
        initSource(mediaSource)
    }

    private suspend fun initSource(mediaSource: MediaSource) = withContext(Dispatchers.IO) {

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
        mediaList.clear()
        mediaList.addAll(remoteSource ?: mutableListOf())
        complete.postValue("complete")
    }

}