package com.yan.hometv.ui

import android.os.Bundle
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import com.bumptech.glide.Glide
import com.yan.hometv.MediaPlayHelper
import com.yan.hometv.bean.MediaItem
import com.yan.hometv.databinding.MediaPlayerBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MediaPlayerFragment : Fragment() {

    private lateinit var binding: MediaPlayerBinding
    private lateinit var mediaPlayHelper: MediaPlayHelper
    private var videoUrl: String? = null
    var rootClick: View.OnClickListener? = null

    companion object {
        const val delay = 1500L
        const val TAG = "MediaPlayerFragment"
    }

    @UnstableApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MediaPlayerBinding.inflate(inflater, container, false)
        binding.playerView.run {
            useController = false
        }
        mediaPlayHelper = getVideoPlayHelper()
        lifecycle.addObserver(mediaPlayHelper)
        if (videoUrl?.isNotEmpty() == true) {
            mediaPlayHelper.setVideoUrl(videoUrl!!)
        }
        binding.root.setOnClickListener {
            rootClick?.onClick(it)
        }
        return binding.root
    }

    fun setMediaItem(mediaItem: MediaItem) {
        if (::mediaPlayHelper.isInitialized) {
            mediaPlayHelper.setVideoUrl(mediaItem.mediaUrl)
            videoUrl = ""
        } else {
            videoUrl = mediaItem.mediaUrl
        }
        TransitionManager.beginDelayedTransition(binding.includeMediaInfo.root)
        binding.includeMediaInfo.run {
            Glide.with(requireActivity()).load(mediaItem.iconUrl).into(mediaIcon)
            mediaName.text = mediaItem.mediaName
            root.isVisible = true
            lifecycleScope.launch {
                delay(delay)
                TransitionManager.beginDelayedTransition(binding.includeMediaInfo.root)
                root.isVisible = false
            }
        }
    }

    fun play() {
        if (::mediaPlayHelper.isInitialized) {
            mediaPlayHelper.play()
        }
    }

    fun pause() {
        if (::mediaPlayHelper.isInitialized) {
            mediaPlayHelper.pause()
        }
    }

    private fun getVideoPlayHelper(): MediaPlayHelper {

        return MediaPlayHelper(binding.playerView, object : Player.Listener {

            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)

                when (playbackState) {
                    Player.STATE_BUFFERING -> {
                        // 缓冲中
                        Log.d(TAG, "STATE_BUFFERING")
                    }

                    Player.STATE_READY -> {
                        // 播放器准备好
                        mediaPlayHelper.play()
                        Log.d(TAG, "STATE_READY")
                    }

                    Player.STATE_ENDED -> {
                        // 播放结束
                        Log.d(TAG, "STATE_ENDED")
                    }

                    Player.STATE_IDLE -> {
                        // 播放器空闲
                        Log.d(TAG, "STATE_IDLE")
                    }
                }
            }

        })


    }

}