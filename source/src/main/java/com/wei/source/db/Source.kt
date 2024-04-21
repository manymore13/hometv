package com.wei.source.db

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "source")
data class Source(

    @PrimaryKey(true)
    @ColumnInfo(name = "source_id")
    var id: Long = 0,

    @ColumnInfo(name = "source_name")
    val name: String,

    @ColumnInfo(name = "source_url")
    val url: String,

    @ColumnInfo(name = "source_refresh_time")
    var refreshTime: Long = 0,

    /**
     * 不做频道ID依赖
     */
    @ColumnInfo(name = "select_channel_id")
    var selectChannelId: Long = 1

) : Parcelable