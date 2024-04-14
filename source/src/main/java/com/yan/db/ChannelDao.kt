package com.yan.db

import androidx.room.Dao
import androidx.room.Query

@Dao
interface ChannelDao {

    fun insertChannel(channel: MutableList<Channel>)

    @Query("SELECT * FROM channel WHERE source_id = :sourceId")
    suspend fun getAllChannel(sourceId: Int): MutableList<Channel>
}