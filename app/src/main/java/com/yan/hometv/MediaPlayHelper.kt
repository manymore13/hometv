package com.yan.hometv

import android.content.Context
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import com.yan.hometv.bean.toSysMediaItem
import com.yan.hometv.ui.MediaPlayerFragment

class MediaPlayHelper(private val context: Context, private val player: Player) :
    DefaultLifecycleObserver {

    private var canRetryConnect = false

    companion object {
        const val TAG = "MediaPlayHelper"
    }

    private val playListener = object : Player.Listener {

        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)

            when (playbackState) {
                Player.STATE_BUFFERING -> {
                    // 缓冲中
                    Log.d(MediaPlayerFragment.TAG, "STATE_BUFFERING")
                }

                Player.STATE_READY -> {
                    // 播放器准备好
                    player.play()
                    canRetryConnect = true
                    Log.d(MediaPlayerFragment.TAG, "STATE_READY")
                }

                Player.STATE_ENDED -> {
                    // 播放结束
                    Log.d(MediaPlayerFragment.TAG, "STATE_ENDED")
                    canRetryConnect = true
                }

                Player.STATE_IDLE -> {
                    // 播放器空闲
                    Log.d(MediaPlayerFragment.TAG, "STATE_IDLE")
                    if (canRetryConnect) {
                        player.prepare()
                        canRetryConnect = false
                    }
                }
            }
        }

        override fun onPlayerError(error: PlaybackException) {
            super.onPlayerError(error)
            error.printStackTrace()
        }
    }

    init {
        player.addListener(playListener)
    }

    fun setMediaItem(mediaItem: com.yan.hometv.bean.MediaItem?) {
        if (mediaItem == null) {
            return
        }
        val sysMediaItem = mediaItem.toSysMediaItem()
        player.setMediaItem(sysMediaItem)
        Log.d(TAG, "setVideoUrl playbackState =${player.playbackState}")
        player.prepare()
    }

    fun addMediaItem(mediaItem: com.yan.hometv.bean.MediaItem) {
        val sysMediaItem = mediaItem.toSysMediaItem()
        player.addMediaItem(sysMediaItem)
        Log.d(TAG, "setVideoUrl playbackState =${player.playbackState}")
    }

    fun prepare() {
        player.prepare()
    }

    fun play() {
        player.play()
    }

    fun pause() {
        player.pause()
    }
}