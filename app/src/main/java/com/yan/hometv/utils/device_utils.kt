package com.yan.hometv.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import com.yan.hometv.App
import com.yan.hometv.R
import java.net.NetworkInterface


fun isW720(): Boolean {
    return App.getContext().resources.getBoolean(R.bool.isW720)
}

fun getVersionName(): String? {
    val context = App.getContext()
    val packageManager: PackageManager = context.packageManager
    val packageInfo: PackageInfo = packageManager.getPackageInfo(context.packageName, 0)
    return packageInfo.versionName
}

fun Context.getConnectivityManager() = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

fun getIpAddress(context: Context) = with(context.getConnectivityManager()) {
    getLinkProperties(activeNetwork)!!.linkAddresses[1].address.hostAddress!!
}