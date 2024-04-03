package com.yan.hometv.utils

import com.tencent.mmkv.MMKV
import com.yan.hometv.App

/**
 * @author manymore13
 * @Description 缓存
 * @date 2024/4/3
 */
fun MMKVInit(){
    val rootDir = MMKV.initialize(App.getContext());
    println("mmkv root: " + rootDir)
}
