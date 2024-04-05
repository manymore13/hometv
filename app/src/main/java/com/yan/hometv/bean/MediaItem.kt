package com.yan.hometv.bean

import android.net.Uri
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes

data class MediaItem(val mediaName: String, val mediaUrl: String, val iconUrl: String)

fun MediaItem.toSysMediaItem(): androidx.media3.common.MediaItem {
    val metaData = MediaMetadata.Builder()
        .setTitle(this.mediaName)
        .setArtworkUri(Uri.parse(this.iconUrl))
        .build()

    return androidx.media3.common.MediaItem.Builder()
        .setMediaMetadata(metaData)
        .setMimeType(MimeTypes.APPLICATION_M3U8)
        .setUri(Uri.parse(mediaUrl))
        .build()
}

fun androidx.media3.common.MediaItem.toMediaItem(): MediaItem {
    val metadata = this.mediaMetadata
    return MediaItem(metadata.title.toString(), "", metadata.artworkUri.toString())
}
