package com.yan.hometv.bean

import android.net.Uri
import androidx.media3.common.MediaMetadata

data class MediaItem(val mediaName: String, val mediaUrl: String, val iconUrl: String)

fun MediaItem.toSysMediaItem(): androidx.media3.common.MediaItem {
    val metaData = MediaMetadata.Builder()
        .setTitle(this.mediaName)
        .setArtworkUri(Uri.parse(this.iconUrl))
        .build()

    return androidx.media3.common.MediaItem.Builder()
        .setMediaMetadata(metaData)
        .setUri(Uri.parse(mediaUrl))
        .build()
}
