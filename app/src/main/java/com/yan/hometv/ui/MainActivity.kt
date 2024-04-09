package com.yan.hometv.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.yan.hometv.MediaViewModel
import com.yan.hometv.R
import com.yan.hometv.databinding.ActivityMainBinding
import com.yan.hometv.ui.medialist.MediaListFragment
import com.yan.hometv.ui.setting.SettingActivity
import com.yan.source.utils.MediaSource

open class MainActivity : AppCompatActivity() {

    companion object {
        const val GROUP_ADD_ID = 520
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, MainActivity::class.java)
            context.startActivity(starter)
        }
    }

    private lateinit var binding: ActivityMainBinding

    private val mediaModel by lazy {
        ViewModelProvider(this)[MediaViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction()
            .replace(R.id.media_list, MediaListFragment())
            .commit()
        mediaModel.init()

        mediaModel.showLoading.observe(this) { show ->
            showLoading(show)
        }
    }
    private fun showLoading(showLoading: Boolean) {
        binding.load.isVisible = showLoading
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
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
                    SettingActivity.start(this)
                    true
                }

                else -> {
                    super.onOptionsItemSelected(item)
                }
            }
        }

    }

}