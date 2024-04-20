package com.wei.liuying.utils

import android.content.Context
import android.net.wifi.WifiManager
import android.text.format.Formatter
import android.widget.Toast
import com.wei.liuying.App
import java.math.BigInteger
import java.security.MessageDigest


fun String.md5(): String {
    val md5 = MessageDigest.getInstance("MD5")
    return BigInteger(1, md5.digest(toByteArray())).toString(16).padStart(32, '0')
}

fun toast(str: String?) {
    if (str == null) {
        return
    }
    Toast.makeText(App.getContext(), str, Toast.LENGTH_LONG).show()
}

fun getCurrentIPAddress(context: Context): String? {
    val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
    if (wifiManager != null && wifiManager.connectionInfo != null) {
        val ipAddress = wifiManager.connectionInfo.ipAddress
        return Formatter.formatIpAddress(ipAddress)
    }
    return "Unavailable"
}

fun isNeedRefreshData(lastTime: Long, refreshTime: Long): Boolean {
    return System.currentTimeMillis() - lastTime > refreshTime
}