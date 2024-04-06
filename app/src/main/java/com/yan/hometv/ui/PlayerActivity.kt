package com.yan.hometv.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.yan.hometv.R
import com.yan.hometv.bean.MediaItem

class PlayerActivity : AppCompatActivity() {

    companion object {
        const val MEDIA_ITEM = "MEDIA_ITEM"

        @JvmStatic
        fun start(context: Context, mediaItem: MediaItem?) {
            val starter = Intent(context, PlayerActivity::class.java).apply {
                putExtra(MEDIA_ITEM, mediaItem)
            }
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        val mediaItem = intent.getParcelableExtra<MediaItem>(MEDIA_ITEM)
        supportFragmentManager.beginTransaction()
            .replace(R.id.media_play, MediaPlayerFragment().apply {
                if (mediaItem != null) {
                    arguments = Bundle().apply {
                        putParcelable(MEDIA_ITEM, mediaItem)
                    }
                }
            }).commit()
    }
}