package com.wei.liuying.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.wei.liuying.App

class NetWorkStatusReceiver(val statusListener: (Boolean) -> Any) : BroadcastReceiver(),
    DefaultLifecycleObserver {


    override fun onReceive(context: Context, intent: Intent) {
        val isConnected = intent.getBooleanExtra(NetworkChangeReceiver.EXTRA_IS_CONNECTED, false)
        statusListener(isConnected)
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        // 注册本地广播接收器
        LocalBroadcastManager.getInstance(App.getContext())
            .registerReceiver(this, IntentFilter(NetworkChangeReceiver.NETWORK_STATUS_CHANGED))
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        // 注销本地广播接收器
        LocalBroadcastManager.getInstance(App.getContext()).unregisterReceiver(this)
    }
}