package com.yan.db

import androidx.room.ColumnInfo
import androidx.room.DatabaseView

@DatabaseView(
    "SELECT c.channel_id, c.channel_name, c.source_id, s.source_name, cg.group_id, cg.group_name, c.channel_icon FROM channel as c " +
            "INNER JOIN channelGroup as cg ON cg.channel_id = c.channel_id " +
            "INNER JOIN source as s ON s.source_id = c.source_id"
)
data class ChannelDetail(
    @ColumnInfo(name = "channel_id")
    val channelId: Int,
    @ColumnInfo(name = "channel_name")
    val channelName: String,

    @ColumnInfo(name = "source_id")
    val sourceId: Int,

    @ColumnInfo(name = "source_name")
    val sourceName: String,

    @ColumnInfo(name = "group_id")
    val groupId: Int,

    @ColumnInfo(name = "group_name")
    val groupName: String,

    @ColumnInfo(name = "channel_icon")
    val channelIcon: String?,
)
