package com.yan.hometv

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tencent.mmkv.MMKV
import com.yan.hometv.bean.MediaItem
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
    }

    fun init() = viewModelScope.launch {
        val mapSource = getSource()
        val oldLogoHost = "https://live.fanmingming.com/"
        val newLogoHost = "https://cdn.jsdelivr.net/gh/fanmingming/live@latest/"
        val remoutSource = mapSource?.map { m3uEntry ->
            MediaItem(
                m3uEntry.title ?: "",
                m3uEntry.location.toString(),
                m3uEntry.metadata["tvg-logo"]?.replace(oldLogoHost, newLogoHost) ?: ""
            )
        }?.toMutableList()
        mediaList.clear()
        mediaList.addAll(remoutSource ?: mutableListOf())
        complete.value = "complete"
    }

    private suspend fun getSource() = withContext(Dispatchers.IO) {
        val kv = MMKV.defaultMMKV()
        mediaRepo.getTvMediaSource()
    }
}