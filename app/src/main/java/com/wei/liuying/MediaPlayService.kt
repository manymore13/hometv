package com.wei.liuying

import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.util.Log
import androidx.annotation.OptIn
import androidx.core.content.ContextCompat
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.wei.liuying.ui.mediaplayer.MediaPlayReceiver


/**
 * @author manymore13
 * @Description media play service
 * @date 2024/4/3
 */
class MediaPlayService : MediaSessionService() {

    private lateinit var mediaSession: MediaSession
    private val mediaPlayReceiver = object : MediaPlayReceiver() {
        override fun onPause() {
            mediaSession.player.pause()
        }

        override fun onDestroy() {
            stopSelf()
        }
    }

    companion object {
        const val TAG = "MediaPlayService"
    }


    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: ")
        initializeSessionAndPlayer()
        ContextCompat.registerReceiver(
            this,
            mediaPlayReceiver,
            IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY).apply {
                addAction(MediaPlayReceiver.ACTION_DESTROY_EVENT)
            }, ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    @OptIn(UnstableApi::class)
    private fun initializeSessionAndPlayer() {
        val dataSourceFactory =
            DefaultHttpDataSource.Factory().apply {
                setAllowCrossProtocolRedirects(true)
                setKeepPostFor302Redirects(true)
            }
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(C.AUDIO_CONTENT_TYPE_UNKNOWN)
            .setUsage(C.USAGE_MEDIA)
            .setAllowedCapturePolicy(C.ALLOW_CAPTURE_BY_SYSTEM)
            .setSpatializationBehavior(C.SPATIALIZATION_BEHAVIOR_AUTO)
            .build()
        val player = ExoPlayer.Builder(this)
            .setAudioAttributes(audioAttributes, true)
            .setMediaSourceFactory(
                DefaultMediaSourceFactory(this).setDataSourceFactory(dataSourceFactory)
            )
            .build()
        player.run {
            playWhenReady = false
        }
        mediaSession = MediaSession.Builder(this, player).build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        Log.d(TAG, controllerInfo.toString())
        return mediaSession
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        val player = mediaSession.player
        if (!player.playWhenReady
            || player.mediaItemCount == 0
            || player.playbackState == Player.STATE_ENDED
        ) {
            // Stop the service if not playing, continue playing in the background
            // otherwise.
            stopSelf()
        }
    }

    override fun onDestroy() {
        mediaSession.run {
            player.release()
            release()
        }
        unregisterReceiver(mediaPlayReceiver)
        super.onDestroy()
    }
}