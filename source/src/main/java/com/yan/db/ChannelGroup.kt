package com.yan.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "channelGroup",
    foreignKeys = [ForeignKey(
        entity = Source::class,
        parentColumns = arrayOf("source_id"),
        childColumns = arrayOf("source_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
class ChannelGroup(

    @ColumnInfo(name = "source_id", index = true)
    val sourceId: Int,

    @ColumnInfo(name = "channel_id")
    val channelId: Int,

    @ColumnInfo(name = "group_name")
    val name: String,
) {
    @PrimaryKey(true)
    @ColumnInfo(name = "group_id")
    var id: Int = 0
}