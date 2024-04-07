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
abstract class BecomingNoisyReceiver : BroadcastReceiver() {

    companion object {
        val intentFilter = IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == AudioManager.ACTION_AUDIO_BECOMING_NOISY) {
            // Pause the playback
            onPause()
        }
    }

    abstract fun onPause()
}