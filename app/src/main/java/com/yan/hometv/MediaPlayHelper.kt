package com.yan.hometv

import android.content.ComponentName
import android.content.Context
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.C
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import com.yan.hometv.bean.toSysMediaItem
import com.yan.hometv.ui.MediaPlayService
import com.yan.hometv.ui.MediaPlayerFragment
import java.lang.StringBuilder

class MediaPlayHelper(private val context: Context) :
    DefaultLifecycleObserver {

    private var canRetryConnect = false

    var player: Player? = null

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
                    player?.play()
                    canRetryConnect = true
                    val isSupportVideo = player?.currentTracks?.isTypeSupported(C.TRACK_TYPE_VIDEO)
                    Log.d(
                        MediaPlayerFragment.TAG,
                        "STATE_READY " + (player?.duration ?: 0) + " $isSupportVideo"
                    )
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
                        player?.prepare()
                        canRetryConnect = false
                    }
                }
            }
        }

        override fun onPlayerError(error: PlaybackException) {
            super.onPlayerError(error)
            error.printStackTrace()
        }

        override fun onEvents(player: Player, events: Player.Events) {
            super.onEvents(player, events)
        }

        override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
            super.onMediaMetadataChanged(mediaMetadata)
        }
    }
    private var outPlayListener: Player.Listener? = null

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        player?.run {
            removeListener(playListener)
            outPlayListener?.let { removeListener(it) }
        }
    }

    fun setPlayerListener(listener: Player.Listener): MediaPlayHelper {
        assert(player != null)
        outPlayListener = listener
        player?.addListener(listener)
        return this
    }

    fun asyncGetPlayer(block: (Player) -> Unit): MediaPlayHelper {
        val sessionToken =
            SessionToken(context, ComponentName(context, MediaPlayService::class.java))
        val controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        controllerFuture.addListener(
            {
                // Call controllerFuture.get() to retrieve the MediaController.
                // MediaController implements the Player interface, so it can be
                // attached to the PlayerView UI component.
                val player = controllerFuture.get()
                this.player = player
                player.addListener(playListener)
                outPlayListener?.let { player.addListener(it) }
                block(player)
            },
            MoreExecutors.directExecutor()
        )
        return this
    }

    fun setMediaItem(mediaItem: com.yan.hometv.bean.MediaItem?) {
        if (mediaItem == null) {
            return
        }
        val sysMediaItem = mediaItem.toSysMediaItem()
        player?.setMediaItem(sysMediaItem)
        Log.d(TAG, "setVideoUrl playbackState =${player?.playbackState}")
        player?.prepare()
    }

    fun addMediaItem(mediaItem: com.yan.hometv.bean.MediaItem) {
        val sysMediaItem = mediaItem.toSysMediaItem()
        player?.addMediaItem(sysMediaItem)
        Log.d(TAG, "setVideoUrl playbackState =${player?.playbackState}")
    }

    fun getMediaDebugInfo(): String {
        val isSupportVideo = player?.currentTracks?.isTypeSupported(C.TRACK_TYPE_VIDEO) ?: false
        val isSupportAudio = player?.currentTracks?.isTypeSupported(C.TRACK_TYPE_AUDIO) ?: false
        val info = StringBuilder()
        if (isSupportVideo) {
            info.append(context.getString(R.string.is_support_video))
        }
        if (isSupportAudio) {
            if (info.isNotBlank()) {
                info.append(",")
            }
            info.append(context.getString(R.string.is_support_audio))
        }
        return info.toString()
    }

    fun prepare() {
        player?.prepare()
    }

    fun play() {
        player?.play()
    }

    fun pause() {
        player?.pause()
    }
}