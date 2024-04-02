package com.yan.hometv

import android.app.Application
import android.content.IntentFilter
import android.net.ConnectivityManager
import com.yan.hometv.receiver.NetworkChangeReceiver

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
        registerReceiver(NetworkChangeReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }
}