package com.yan.hometv.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager

/**
 * @author manymore13
 * @Description BecomingNoisyReceiver
 * @date 2024/4/7
 */
abstract class MediaPlayReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_DESTROY_EVENT = "ACTION_DESTROY_EVENT"
    }

    override fun onReceive(context: Context, intent: Intent) {
        when(intent.action){
            AudioManager.ACTION_AUDIO_BECOMING_NOISY->{
                // Pause the playback
                onPause()
            }
            ACTION_DESTROY_EVENT->{
                onDestroy()
            }
        }
    }

    abstract fun onPause()

    abstract fun onDestroy()
}