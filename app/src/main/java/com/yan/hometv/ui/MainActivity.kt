package com.yan.hometv.ui

import android.os.Bundle
import android.view.KeyEvent
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.yan.hometv.MediaViewModel
import com.yan.hometv.R
import com.yan.hometv.databinding.ActivityMainBinding
import com.yan.hometv.ui.helper.IMediaDeviceUi
import com.yan.hometv.utils.isW720
import com.yan.hometv.utils.showFragment

open class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val uiHelper: IMediaDeviceUi by lazy {
        IMediaDeviceUi.getMediaDevice(this, R.id.media_play, R.id.media_list)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        uiHelper.onCreate(savedInstanceState)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        uiHelper.onKeyUp(keyCode, event)
        return super.onKeyUp(keyCode, event)
    }

    override fun onBackPressed() {
        if (!uiHelper.onBackReturn()) {
            super.onBackPressed()
        }
    }

}