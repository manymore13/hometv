package com.yan.hometv.ui

import android.util.Log
import androidx.media3.exoplayer.ExoPlayer
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

    override fun onCreate() {
        super.onCreate()
        val player = ExoPlayer.Builder(this).build()
        mediaSession = MediaSession.Builder(this, player)
            .build()
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