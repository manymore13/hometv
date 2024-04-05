package com.yan.hometv.ui.helper

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.yan.hometv.MediaViewModel
import com.yan.hometv.bean.MediaItem
import com.yan.hometv.databinding.ActivityMainBinding
import com.yan.hometv.ui.MediaListFragment
import com.yan.hometv.ui.MediaPlayerFragment

abstract class MediaUiHelper(
    protected val activity: FragmentActivity,
    @IdRes val mediaPlayResId: Int, @IdRes val mediaListResId: Int
) : IMediaDeviceUi {

    protected val mediaPlayerFragment = MediaPlayerFragment()
    protected val mediaListFragment = MediaListFragment()

    protected lateinit var binding: ActivityMainBinding

    protected val mediaModel by lazy {
        ViewModelProvider(activity)[MediaViewModel::class.java]
    }

    override fun bindView(): View {
        binding = ActivityMainBinding.inflate(activity.layoutInflater)
        return binding.root
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {

        mediaModel.selectMediaLiveData.observe(activity) {
            onClickSelectMediaItem(it)
        }

        mediaModel.complete.observe(activity) {
            mediaSourceLoadedComplete()
        }

        mediaModel.showLoading.observe(activity) { show ->
            showLoading(show)
        }

    }

    @CallSuper
    override fun onClickSelectMediaItem(mediaItem: MediaItem) {
//        mediaPlayerFragment.setMediaItem(mediaItem)
    }

    private fun showLoading(showLoading: Boolean) {
        binding.load?.isVisible = showLoading
    }

}
