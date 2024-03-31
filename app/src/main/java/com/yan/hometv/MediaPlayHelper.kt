package com.yan.hometv

import android.content.Context
import android.net.Uri
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

class MediaPlayHelper(private var playerView: PlayerView, private val listener: Player.Listener) :
    DefaultLifecycleObserver {

    private val context: Context = playerView.context
    private lateinit var player: ExoPlayer

    private var videoUrl: String? = null

    private fun initPlayer(): Player {
        player = ExoPlayer.Builder(context)
            .build().apply {
                addListener(listener)
                playWhenReady = true
                playerView.player = this
            }
        return player
    }

    fun setVideoUrl(url: String) {
        videoUrl = url
        if (::player.isInitialized) {
            player.setMediaItem(MediaItem.fromUri(Uri.parse(videoUrl)))
            player.prepare()
        }
    }

    fun play() {
        if (::player.isInitialized) {
            player.play()
        }
    }

    fun pause() {
        if (::player.isInitialized) {
            player.pause()
        }
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        initPlayer()
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        if (::player.isInitialized) {
            player.play()
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        if (::player.isInitialized) {
            player.pause()
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        if (::player.isInitialized) {
            player.release()
        }
    }


}