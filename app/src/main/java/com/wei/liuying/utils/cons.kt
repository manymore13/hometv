package com.wei.liuying.utils

import com.wei.liuying.App
import com.wei.liuying.R

/**
 * @author manymore13
 * @Description 静态值
 * @date 2024/4/16
 */

val SOURCE_UPDATE_TIME_KEY: String by lazy {
    App.getContext().getString(R.string.source_update_time_key)
}
const val SELECTED_SOURCE_NAME = "selected_source_name"