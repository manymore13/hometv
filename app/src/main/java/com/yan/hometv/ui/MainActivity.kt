package com.yan.hometv.ui

import android.os.Bundle
import android.view.KeyEvent
import android.view.WindowManager
import android.window.OnBackInvokedDispatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.yan.hometv.MediaViewModel
import com.yan.hometv.R
import com.yan.hometv.databinding.ActivityMainBinding
import com.yan.hometv.utils.hideFragment
import com.yan.hometv.utils.isTv
import com.yan.hometv.utils.showFragment

class MainActivity : AppCompatActivity() {

    private val mediaPlayerFragment = MediaPlayerFragment()
    private val mediaListFragment = MediaListFragment()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        if (savedInstanceState == null) {

            val fragmentManager = supportFragmentManager

            val needHideFragment = if (isTv()) mediaListFragment else mediaPlayerFragment

            fragmentManager.beginTransaction()
                .replace(R.id.media_list, mediaListFragment, MediaListFragment.TAG)
                .replace(R.id.media_play, mediaPlayerFragment, MediaPlayerFragment.TAG)
                .hide(needHideFragment)
                .commit()
        }

        val mediaModel = ViewModelProvider(this)[MediaViewModel::class.java]
        mediaModel.selectMediaLiveData.observe(this) {
            mediaPlayerFragment.setVideoUrl(it.mediaUrl)
            if (isTv()) {
                hideFragment(supportFragmentManager, mediaListFragment)
            } else {
                showFragment(supportFragmentManager, mediaPlayerFragment)
            }
        }
        mediaModel.complete.observe(this) {
            mediaPlayerFragment.setVideoUrl(mediaModel.mediaList.get(0).mediaUrl)
        }
        binding.mediaPlay.setOnClickListener {
            showFragment(supportFragmentManager, mediaListFragment)
        }
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if ((keyCode == KeyEvent.KEYCODE_MENU) && isTv()) {
            showFragment(supportFragmentManager, mediaListFragment)
            return true
        }
        return super.onKeyUp(keyCode, event)
    }

    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
            if (supportFragmentManager.findFragmentByTag(MediaPlayerFragment.TAG) != null) {
                mediaPlayerFragment.pause()
            }
        } else {
            super.onBackPressed()
        }
    }

}