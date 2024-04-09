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
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.yan.hometv.bean.MediaItem
import com.yan.hometv.bean.toSysMediaItem
import com.yan.hometv.ui.mediaplayer.MediaPlayerFragment
import com.yan.hometv.utils.getErrorCodeName
import com.yan.hometv.utils.toast
import java.lang.StringBuilder

class MediaPlayHelper(private val context: Context) :
    DefaultLifecycleObserver {

    private var canRetryConnect = false

    private var controllerFuture: ListenableFuture<MediaController>? = null

    private var sysMediaItem: androidx.media3.common.MediaItem? = null

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
            val info = "${error.errorCode}:${getErrorCodeName(error.errorCode)}"
            toast(info)
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
    var asyncGetPlayer: ((Player) -> Unit)? = null

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        asyncGetPlayer()
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        controllerFuture?.let { MediaController.releaseFuture(it) }
    }

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

    private fun asyncGetPlayer(): MediaPlayHelper {
        val sessionToken =
            SessionToken(context, ComponentName(context, MediaPlayService::class.java))
        val controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        this.controllerFuture = controllerFuture
        controllerFuture.addListener(
            {
                // Call controllerFuture.get() to retrieve the MediaController.
                // MediaController implements the Player interface, so it can be
                // attached to the PlayerView UI component.
                val player = controllerFuture.get()
                this.player = player
                player.addListener(playListener)

                this.sysMediaItem = null
                outPlayListener?.let { player.addListener(it) }
                asyncGetPlayer?.let { it(player) }

                this.sysMediaItem?.let {
                    player.setMediaItem(it)
                    player.prepare()
                }
            },
            MoreExecutors.directExecutor()
        )
        return this
    }

    fun setMediaItem(mediaItem: MediaItem?) {
        if (mediaItem == null) {
            return
        }
        val sysMediaItem = mediaItem.toSysMediaItem()
        if (player == null) {
            this.sysMediaItem = sysMediaItem
            return
        }
        player?.run {
            setMediaItem(sysMediaItem)
            prepare()
            play()
            Log.d(TAG, "setVideoUrl playbackState =${player?.playbackState}")
        }
    }

    fun addMediaItem(mediaItem: MediaItem) {
        val sysMediaItem = mediaItem.toSysMediaItem()
        player?.addMediaItem(sysMediaItem)
        Log.d(TAG, "setVideoUrl playbackState =${player?.playbackState}")
    }

    fun isSupportVideo(): Boolean {
        return player?.currentTracks?.isTypeSupported(C.TRACK_TYPE_VIDEO) ?: false
    }

    fun getMediaTypeInfo(): String {
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