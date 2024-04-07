package com.yan.hometv.utils

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.yan.hometv.App
import com.yan.hometv.R


fun isW720(): Boolean {
    return App.getContext().resources.getBoolean(R.bool.isW720)
}

fun getVersionName(): String? {
    val context = App.getContext()
    val packageManager: PackageManager = context.packageManager
    val packageInfo: PackageInfo = packageManager.getPackageInfo(context.packageName, 0)
    return packageInfo.versionName
}