package com.wei.liuying.utils

import androidx.media3.common.PlaybackException
import androidx.media3.common.PlaybackException.*
import com.wei.liuying.App
import com.wei.liuying.R

fun getString(resId: Int): String {
    return App.getContext().resources.getString(resId)
}

fun getErrorCodeName(@ErrorCode errorCode: Int): String {
    return when (errorCode) {
        ERROR_CODE_IO_NETWORK_CONNECTION_FAILED -> {
            getString(R.string.error_code_io_network_connection_failed)
        }

        ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT -> {
            getString(R.string.error_code_io_network_connection_failed)
        }

        else -> PlaybackException.getErrorCodeName(errorCode)
    }
}