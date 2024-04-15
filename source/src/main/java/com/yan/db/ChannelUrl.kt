package com.yan.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "channelUrl",
    primaryKeys = ["channel_id", "source_id"],
    foreignKeys = [ForeignKey(
        entity = Source::class,
        parentColumns = arrayOf("source_id"),
        childColumns = arrayOf("source_id"),
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = Channel::class,
        parentColumns = arrayOf("channel_id"),
        childColumns = arrayOf("channel_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
class ChannelUrl(

    @ColumnInfo(name = "channel_id", index = true)
    val channelId: Int,

    @ColumnInfo(name = "source_id", index = true)
    val sourceId: Int,

    @ColumnInfo(name = "channel_url")
    val url: String

)