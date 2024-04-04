package com.yan.hometv.ui

import android.os.Bundle
import android.view.ContextMenu
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.yan.hometv.MediaViewModel
import com.yan.hometv.R
import com.yan.hometv.databinding.ActivityMainBinding
import com.yan.hometv.ui.helper.IMediaDeviceUi
import com.yan.hometv.utils.isW720
import com.yan.hometv.utils.showFragment

open class MainActivity : AppCompatActivity() {

    private val uiHelper: IMediaDeviceUi by lazy {
        IMediaDeviceUi.getMediaDevice(this, R.id.media_play, R.id.media_list)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(uiHelper.bindView())
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        uiHelper.onCreate(savedInstanceState)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        uiHelper.onKeyUp(keyCode, event)
        return super.onKeyUp(keyCode, event)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.media_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.insert_media_source -> {
                AddSourceFragment().show(supportFragmentManager,"add_source")
                true
            }

            R.id.setting -> {
                Toast.makeText(this, "开发中...", Toast.LENGTH_SHORT).show()
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onBackPressed() {
        if (!uiHelper.onBackReturn()) {
            super.onBackPressed()
        }
    }

}