package com.yan.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "source")
class Source(

    @ColumnInfo(name = "source_name")
    val name: String,

    @ColumnInfo(name = "source_url")
    val url: String
) {
    @PrimaryKey(true)
    @ColumnInfo(name = "source_id")
    var id: Long = 0
}