package com.yan.hometv.ui

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.yan.hometv.MediaViewModel
import com.yan.hometv.R
import com.yan.hometv.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val mediaPlayerFragment = MediaPlayerFragment()
    val mediaListFragment = MediaListFragment()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.content, mediaListFragment)
                .addToBackStack("media_list")
                .commit()
        }

        val mediaModel = ViewModelProvider(this)[MediaViewModel::class.java]
        mediaModel.selectMediaLiveData.observe(this) {
            mediaPlayerFragment.setVideoUrl(it.mediaUrl)
            supportFragmentManager.beginTransaction()
                .replace(R.id.content, mediaPlayerFragment)
                .addToBackStack("media_play")
                .commit()
        }

//        val videoUrl =
//            "http://[2409:8087:7001:20:3::6]:80/dbiptv.sn.chinamobile.com/PLTV/88888888/224/3221226195/index.m3u8"
//        mediaPlayerFragment.setVideoUrl(videoUrl)
//        val mediaItem =
//            MediaItem.fromUri("http://[2409:8087:7001:20:3::6]:80/dbiptv.sn.chinamobile.com/PLTV/88888888/224/3221226195/index.m3u8")
//        val mediaItem = MediaItem.fromUri("https://storage.googleapis.com/exoplayer-test-media-0/play.mp3")
//        player.setMediaItem(mediaItem)
//        player.prepare()

    }

}