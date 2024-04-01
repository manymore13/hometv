package com.yan.hometv

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yan.hometv.bean.MediaItem
import com.yan.source.utils.MediaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.bjoernpetersen.m3u.model.M3uEntry

class MediaViewModel(application: Application) : AndroidViewModel(application) {

    val complete = MutableLiveData<String>()

    val mediaList = mutableListOf<MediaItem>()

    val selectMediaLiveData = MutableLiveData<MediaItem>()

    val mediaRepo: MediaRepository by lazy {
        MediaRepository()
    }

    fun init() = viewModelScope.launch {
        val mapSource = getSource()
        val oldLogoHost = "https://live.fanmingming.com/"
        val newLogoHost = "https://cdn.jsdelivr.net/gh/fanmingming/live@latest/"
        val remoutSource = mapSource.map { m3uEntry ->
            MediaItem(
                m3uEntry.title ?: "",
                m3uEntry.location.toString(),
                m3uEntry.metadata["tvg-logo"]?.replace(oldLogoHost,newLogoHost) ?: ""
            )
        }.toMutableList()
        mediaList.clear()
        mediaList.addAll(remoutSource)
        complete.value = "complete"
//        val mediaSourceMapData:MutableList<M3uEntry> =
    }

    private suspend fun getSource() = withContext(Dispatchers.IO) {
        mediaRepo.getMediaSource()
    }
}