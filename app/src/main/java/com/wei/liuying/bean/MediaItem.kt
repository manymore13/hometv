package com.wei.liuying.bean

import android.net.Uri
import android.os.Parcelable
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes
import com.wei.source.db.Channel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MediaItem(
    /**
     * channel id
     */
    val id: Long,
    val mediaName: String,
    var mediaUrl: String,
    val iconUrl: String,
    val sourceId: Long
) : Parcelable

fun Channel.toMediaItem(): MediaItem {
    return MediaItem(
        id = id, mediaName = name,
        mediaUrl = "", iconUrl = icon ?: "", sourceId = sourceId
    )
}

fun MediaItem.toSysMediaItem(): androidx.media3.common.MediaItem {
    val metaData = MediaMetadata.Builder()
        .setTitle(this.mediaName)
        .setArtworkUri(Uri.parse(this.iconUrl))
        .build()

    return androidx.media3.common.MediaItem.Builder()
        .setMediaMetadata(metaData)
        .setMimeType(MimeTypes.APPLICATION_M3U8)
        .setUri(Uri.parse(mediaUrl))
        .setTag(this)
        .build()
}

fun androidx.media3.common.MediaItem.toMediaItem(): MediaItem {
    val metadata = this.mediaMetadata
    val tag = this.localConfiguration?.tag
    var id: Long = 0L
    var sourceId: Long = 0L
    if (tag != null) {
        val mediaItem: MediaItem = this.localConfiguration?.tag as MediaItem
        id = mediaItem.id
        sourceId = mediaItem.sourceId
    }

    return MediaItem(
        id = id,
        mediaName = metadata.title.toString(),
        mediaUrl = "",
        iconUrl = metadata.artworkUri.toString(), sourceId = sourceId
    )
}
