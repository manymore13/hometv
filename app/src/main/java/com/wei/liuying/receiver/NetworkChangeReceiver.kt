package com.wei.liuying.receiver

import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class NetworkChangeReceiver(
    private val context: Context,
    private val networkCallBack: ConnectivityManager.NetworkCallback
) : DefaultLifecycleObserver {

    private val connectivityManager by lazy {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    companion object {
        const val NETWORK_STATUS_CHANGED = "network_status_changed"
        const val EXTRA_IS_CONNECTED = "is_connected"
    }

    init {
        connectivityManager.registerDefaultNetworkCallback(networkCallBack)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        unRegister()
    }

    fun unRegister() {
        connectivityManager.unregisterNetworkCallback(networkCallBack)
    }
}