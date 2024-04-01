package com.yan.hometv

import android.app.Application

class App : Application() {

    companion object {

        private lateinit var instance: App

        fun getContext(): App {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}