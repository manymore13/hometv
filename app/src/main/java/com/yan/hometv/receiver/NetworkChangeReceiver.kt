package com.yan.hometv.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class NetworkChangeReceiver: BroadcastReceiver() {

    companion object {
        const val NETWORK_STATUS_CHANGED = "network_status_changed"
        const val EXTRA_IS_CONNECTED = "is_connected"
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION == intent.action) {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo

            val isConnected = networkInfo != null && networkInfo.isConnected

            // 发送本地广播
            val localIntent = Intent(NETWORK_STATUS_CHANGED)
            localIntent.putExtra(EXTRA_IS_CONNECTED, isConnected)
            LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent)
        }
    }
}