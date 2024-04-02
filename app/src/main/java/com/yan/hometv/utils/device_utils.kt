package com.yan.hometv.utils

import com.yan.hometv.App
import com.yan.hometv.R

fun isW720(): Boolean {
    return App.getContext().resources.getBoolean(R.bool.isW720)
}