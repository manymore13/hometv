package com.wei.liuying

import android.app.Application
import android.content.IntentFilter
import android.net.ConnectivityManager
import com.wei.liuying.receiver.NetworkChangeReceiver
import com.wei.liuying.utils.MMKVInit

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
        MMKVInit()
    }
}