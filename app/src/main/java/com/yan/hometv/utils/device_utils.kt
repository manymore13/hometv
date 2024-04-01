package com.yan.hometv.utils

import com.yan.hometv.App
import com.yan.hometv.R

fun isTv(): Boolean {
    return App.getContext().resources.getBoolean(R.bool.is_tv)
}