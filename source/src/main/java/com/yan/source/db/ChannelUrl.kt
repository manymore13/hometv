package com.yan.source.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "channelUrl",
    foreignKeys = [ForeignKey(
        entity = Channel::class,
        parentColumns = arrayOf("channel_id"),
        childColumns = arrayOf("channel_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
class ChannelUrl(

    @ColumnInfo(name = "channel_id", index = true)
    var channelId: Long,

    @PrimaryKey
    @ColumnInfo(name = "channel_url")
    val url: String

)