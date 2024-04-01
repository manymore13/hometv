package com.yan.hometv.ui

import android.os.Bundle
import android.view.KeyEvent
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.yan.hometv.MediaViewModel
import com.yan.hometv.R
import com.yan.hometv.databinding.ActivityMainBinding
import com.yan.hometv.databinding.ActivityMainLandBinding
import com.yan.hometv.utils.FragmentUtils

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
            FragmentUtils.add(
                supportFragmentManager,
                mediaListFragment,
                R.id.list,
            )
        }

        val mediaModel = ViewModelProvider(this)[MediaViewModel::class.java]
        mediaModel.selectMediaLiveData.observe(this) {
            FragmentUtils.hide(mediaListFragment)
            FragmentUtils.add(
                supportFragmentManager,
                mediaPlayerFragment,
                R.id.detail,
                true,
                R.anim.right_in,
                R.anim.left_out,
                R.anim.left_in,
                R.anim.right_out
            )
            mediaPlayerFragment.setVideoUrl(it.mediaUrl)
        }

    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            FragmentUtils.showHide(mediaListFragment)
            return true
        }
        return super.onKeyUp(keyCode, event)
    }

}