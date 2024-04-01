package com.yan.hometv.ui

import androidx.fragment.app.Fragment
import com.yan.hometv.App
import com.yan.hometv.R

class UiHelper {

    private val mediaPlayerFragment = MediaPlayerFragment()

    private val mediaListFragment = MediaListFragment()

    private val isTv: Boolean by lazy {
        App.getContext().resources.getBoolean(R.bool.is_tv)
    }

    fun getFistShowFragment(): Fragment {
        return if (isTv) {
            mediaPlayerFragment
        } else {
            mediaListFragment
        }
    }
}