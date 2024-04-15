package com.yan.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "channel",
    foreignKeys = [ForeignKey(
        entity = Source::class,
        parentColumns = arrayOf("source_id"),
        childColumns = arrayOf("source_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
class Channel(

    @ColumnInfo(name = "channel_name")
    val name: String,

    @ColumnInfo(name = "channel_icon")
    val icon: String?,

    @ColumnInfo(name = "source_id", index = true)
    val sourceId: Long,

    @ColumnInfo(name = "group_name")
    val groupName: String,
) {
    @PrimaryKey(true)
    @ColumnInfo(name = "channel_id")
    var id: Long = 0
}