package com.yan.hometv.utils

import java.math.BigInteger
import java.security.MessageDigest

fun String.md5():String{
    val md5 = MessageDigest.getInstance("MD5")
    return BigInteger(1, md5.digest(toByteArray())).toString(16).padStart(32, '0')
}

