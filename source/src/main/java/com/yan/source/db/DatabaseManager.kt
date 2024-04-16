package com.yan.source.db

import android.content.Context
import androidx.room.Room

/**
 * @author manymore13
 * @Description DatabaseManager
 * @date 2024/4/15
 */
object DatabaseManager {

    private var appDatabase: AppDataBase? = null

    @Synchronized
    fun getInstance(context: Context): AppDataBase {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(
                context.applicationContext,
                AppDataBase::class.java,
                "app_database"
            ).build()
        }
        return appDatabase!!
    }
}