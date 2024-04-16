package com.yan.source.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Source::class, Channel::class, ChannelUrl::class],
    views = [ChannelDetail::class], exportSchema = false,
    version = 1
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun getChannelDao(): ChannelDao
}