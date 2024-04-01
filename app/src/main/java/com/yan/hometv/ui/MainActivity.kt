package com.yan.hometv.ui

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.yan.hometv.MediaViewModel
import com.yan.hometv.R
import com.yan.hometv.databinding.ActivityMainBinding
import com.yan.hometv.utils.FragmentUtils

class MainActivity : AppCompatActivity() {

    val mediaPlayerFragment = MediaPlayerFragment()
    val mediaListFragment = MediaListFragment()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        if (savedInstanceState == null) {
            FragmentUtils.add(
                supportFragmentManager,
                mediaListFragment,
                R.id.content,
//                R.animator.slide_in_from_right,
//                R.animator.slide_out_to_left,
//                R.animator.slide_in_from_left,
//                R.animator.slide_out_to_right
            )
        }

        val mediaModel = ViewModelProvider(this)[MediaViewModel::class.java]
        mediaModel.selectMediaLiveData.observe(this) {
            FragmentUtils.add(
                supportFragmentManager,
                mediaPlayerFragment,
                R.id.content,
                true,
                R.anim.right_in,
                R.anim.left_out,
                R.anim.left_in,
                R.anim.right_out
            )
            mediaPlayerFragment.setVideoUrl(it.mediaUrl)
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