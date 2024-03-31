package com.yan.hometv.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import com.yan.hometv.MediaPlayHelper
import com.yan.hometv.databinding.MediaPlayerBinding

class MediaPlayerFragment : Fragment() {

    private lateinit var binding: MediaPlayerBinding
    private lateinit var mediaPlayHelper: MediaPlayHelper
    private var videoUrl: String? = null

    companion object{
        const val TAG = "VideoPlayerFragment"
    }

    @UnstableApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MediaPlayerBinding.inflate(inflater, container, false)
        binding.playerView.run {
            useController = true
        }
        mediaPlayHelper = getVideoPlayHelper()
        lifecycle.addObserver(mediaPlayHelper)
        if (videoUrl?.isNotEmpty() == true) {
            mediaPlayHelper.setVideoUrl(videoUrl!!)
        }

        return binding.root
    }

    fun setVideoUrl(url: String) {
        if (::mediaPlayHelper.isInitialized) {
            mediaPlayHelper.setVideoUrl(url)
            videoUrl = ""
        } else {
            videoUrl = url
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
                        Log.d(TAG,"STATE_BUFFERING")
                    }

                    Player.STATE_READY -> {
                        // 播放器准备好
                        Log.d(TAG,"STATE_READY")
                    }

                    Player.STATE_ENDED -> {
                        // 播放结束
                        Log.d(TAG,"STATE_ENDED")
                    }

                    Player.STATE_IDLE -> {
                        // 播放器空闲
                        Log.d(TAG,"STATE_IDLE")
                    }
                }
            }

        })


    }

}