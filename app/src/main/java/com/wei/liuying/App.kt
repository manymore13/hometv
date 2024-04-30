package com.wei.liuying

import android.app.Application
import android.content.IntentFilter
import android.net.ConnectivityManager
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.mmkv.BuildConfig
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
//        CrashReport.initCrashReport(this, "f3d531896c", false);
        instance = this
        MMKVInit()
    }
}