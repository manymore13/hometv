package com.yan.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
abstract class ChannelDao {

    @Insert
    abstract suspend fun insertChannels(channel: MutableList<Channel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertSource(source: Source): Long

    @Query("Select * from source where source_name = :sourceName limit 1")
    abstract suspend fun getSourceByName(sourceName: String): Source?

    @Query("Select * from source where source_id = :sourceId")
    abstract suspend fun getSourceById(sourceId: Long): Source?

    suspend fun hasSource(sourceName: String): Boolean {
        return getSourceByName(sourceName) != null
    }

    @Insert
    abstract suspend fun insertChannelUrl(channelUrl: ChannelUrl)

    @Query("SELECT * FROM ChannelDetail")
    abstract suspend fun getAllChannelDetail(): MutableList<ChannelDetail>

    @Query("SELECT * FROM channel WHERE source_id = :sourceId")
    abstract suspend fun getAllChannel(sourceId: Long): MutableList<Channel>
}