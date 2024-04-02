package com.yan.hometv.ui.helper

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.yan.hometv.MediaViewModel
import com.yan.hometv.bean.MediaItem
import com.yan.hometv.ui.MediaListFragment
import com.yan.hometv.ui.MediaPlayerFragment

abstract class MediaUiHelper(
    protected val activity: FragmentActivity,
    @IdRes val mediaPlayResId: Int, @IdRes val mediaListResId: Int
) : IMediaDeviceUi {

    protected val mediaPlayerFragment = MediaPlayerFragment()
    protected val mediaListFragment = MediaListFragment()
    private val mediaModel by lazy {
        ViewModelProvider(activity)[MediaViewModel::class.java]
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {

        if (savedInstanceState == null) {
            val fragmentManager = activity.supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(mediaListResId, mediaListFragment, MediaListFragment.TAG)
                .replace(mediaPlayResId, mediaPlayerFragment, MediaPlayerFragment.TAG)
                .hide(mediaPlayerFragment)
                .commit()
        }

        mediaModel.selectMediaLiveData.observe(activity) {
            onClickSelectMediaItem(it)
        }

        mediaModel.complete.observe(activity) {
            mediaSourceLoadedComplete()
        }

    }

    @CallSuper
    override fun onClickSelectMediaItem(mediaItem: MediaItem) {
        mediaPlayerFragment.setMediaItem(mediaItem)
    }

}
