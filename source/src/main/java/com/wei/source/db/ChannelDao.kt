package com.wei.source.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ChannelDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertChannels(channel: MutableList<Channel>): MutableList<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertSource(source: Source): Long

    @Update
    abstract suspend fun updateSource(source: Source):Int

    @Query("UPDATE source SET select_channel_id = :selectChannelId WHERE source_id = :sourceId")
    abstract suspend fun updateSelectChannelFromSource(sourceId: Long, selectChannelId: Long): Int

    @Query("Select * from source where source_name = :sourceName limit 1")
    abstract suspend fun getSourceByName(sourceName: String): Source?

    @Query("Select * from source where source_id = :sourceId")
    abstract suspend fun getSourceById(sourceId: Long): Source?

    @Query("Select * from source")
    abstract fun getAllSourceFlow(): Flow<MutableList<Source>>

    @Query("Select * from source")
    abstract suspend fun getAllSource(): MutableList<Source>

    @Delete
    abstract suspend fun deleteSources(sourceList: MutableList<Source>): Int

    @Query("delete from channel where source_id = :sourceId")
    abstract suspend fun deleteChannelBySourceId(sourceId: Long): Int

    suspend fun hasSource(sourceName: String): Boolean {
        return getSourceByName(sourceName) != null
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertChannelUrl(channelUrl: MutableList<ChannelUrl>)

    @Query("SELECT * FROM channelUrl WHERE channel_id = :channelId")
    abstract suspend fun getChannelUrls(channelId: Long): MutableList<ChannelUrl>

    @Query("SELECT * FROM channelUrl WHERE channel_url = :url")
    abstract suspend fun getChannelUrl(url: String): ChannelUrl

    @Query("SELECT * FROM ChannelDetail")
    abstract suspend fun getAllChannelDetail(): MutableList<ChannelDetail>

    @Query("SELECT * FROM channel WHERE source_id = :sourceId")
    abstract suspend fun getChannelBySourceId(sourceId: Long): MutableList<Channel>

    @Query("SELECT * FROM channel WHERE source_id = :sourceId")
    abstract fun getChannelFlowBySourceId(sourceId: Long): Flow<MutableList<Channel>>

    @Query("SELECT * FROM channel WHERE channel_id = :channelId")
    abstract suspend fun getChannelById(channelId: Long): Channel?

    @Query("SELECT * FROM channel WHERE channel_name = :channelName")
    abstract suspend fun getChannelByName(channelName: String): Channel?

    @Query("SELECT * FROM channel WHERE source_id = :sourceId AND channel_name = :channelName limit 1")
    abstract suspend fun getChannelBySourceIdAndChannelName(
        sourceId: Long, channelName: String
    ): Channel?

    @Query("SELECT count(*)  FROM channel WHERE source_id = :sourceId")
    abstract suspend fun countChannel(sourceId: Long): Long
}