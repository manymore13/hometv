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
import com.tencent.mmkv.MMKV
import com.yan.hometv.MediaViewModel
import com.yan.hometv.R
import com.yan.hometv.databinding.ActivityMainBinding
import com.yan.hometv.ui.helper.IMediaDeviceUi
import com.yan.hometv.utils.isW720
import com.yan.hometv.utils.showFragment
import com.yan.source.utils.MediaSource

open class MainActivity : AppCompatActivity() {

    companion object {
        const val GROUP_ADD_ID = 520
    }

    private val uiHelper: IMediaDeviceUi by lazy {
        IMediaDeviceUi.getMediaDevice(this, R.id.media_play, R.id.media_list)
    }

    private val mediaModel by lazy {
        ViewModelProvider(this)[MediaViewModel::class.java]
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        val sourceNameSet = MediaSource.getSourceNameList()
        sourceNameSet?.forEach { sourceName ->
            menu.add(GROUP_ADD_ID, 1, 0, sourceName)
        }
        inflater.inflate(R.menu.media_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val groupId = item.groupId
        if (groupId == GROUP_ADD_ID) {
            val title = item.title
            mediaModel.loadSource(title.toString())
            return true
        } else {
            return when (item.itemId) {
                R.id.insert_media_source -> {
                    AddSourceFragment().show(supportFragmentManager, "add_source")
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

    }

    override fun onBackPressed() {
        if (!uiHelper.onBackReturn()) {
            super.onBackPressed()
        }
    }

}