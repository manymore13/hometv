package com.yan.db

import androidx.room.DatabaseView

@DatabaseView(
    "SELECT c.channel_id,c.channel_name, s.source_id, s.source_name, cg.group_id, cg.group_name, channel_icon FROM channel as c " +
            "JOIN channelGroup as cg ON cg.channel_id = c.channel_id " +
            "JOIN source as s ON s.source_id = c.source_id"
)
data class ChannelDetail(
    val channelId: Int,
    val channelName: String,

    val sourceId: Int,
    val sourceName: String,

    val groupId: Int,
    val groupName: String,

    val channelIcon: String?,
)
