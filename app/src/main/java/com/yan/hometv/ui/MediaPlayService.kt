package com.yan.hometv.ui

import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService

/**
 * @author manymore13
 * @Description MediaPlayService
 * @date 2024/4/3
 */
class MediaPlayService : MediaSessionService() {

    private var mediaSession: MediaSession? = null

    companion object {
        const val TAG = "MediaPlayService"
    }

    @OptIn(UnstableApi::class)
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: ")
        val dataSourceFactory =
            DefaultHttpDataSource.Factory().apply {
                setAllowCrossProtocolRedirects(true)
                setKeepPostFor302Redirects(true)
            }
        val player = ExoPlayer.Builder(this)
            .setMediaSourceFactory(
                DefaultMediaSourceFactory(this).setDataSourceFactory(dataSourceFactory)
            )
            .build()
        mediaSession = MediaSession.Builder(this, player).build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        Log.d(TAG, controllerInfo.toString())
        return mediaSession
    }

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }
}