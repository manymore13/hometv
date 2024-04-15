package com.yan.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ChannelDao {

    @Insert
    fun insertChannels(channel: MutableList<Channel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSource(source: Source): Long

    @Insert
    fun insertChannelUrl(channelUrl: ChannelUrl)

    @Insert
    fun insertChannelGroup(channelGroup: ChannelGroup)

    @Query("SELECT * FROM ChannelDetail")
    suspend fun getAllChannelDetail(): MutableList<ChannelDetail>

    @Query("SELECT * FROM channel WHERE source_id = :sourceId")
    suspend fun getAllChannel(sourceId: Int): MutableList<Channel>
}