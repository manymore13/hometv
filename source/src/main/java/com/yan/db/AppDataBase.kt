package com.yan.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Source::class, Channel::class, ChannelGroup::class, ChannelUrl::class],
    views = [ChannelDetail::class],
    version = 1
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun getChannelDao(): ChannelDao
}