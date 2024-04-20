package com.wei.liuying.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.wei.liuying.MediaViewModel
import com.wei.liuying.R
import com.wei.liuying.databinding.ActivityMainBinding
import com.wei.liuying.ui.medialist.MediaListFragment
import com.wei.liuying.ui.setting.SettingActivity
import kotlinx.coroutines.launch

open class MainActivity : AppCompatActivity() {

    private var menu: Menu? = null

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
        supportFragmentManager.beginTransaction().replace(R.id.media_list, MediaListFragment())
            .commit()
        mediaModel.initSource()

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
        this.menu = menu
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.media_menu, menu)

        lifecycleScope.launch {
            mediaModel.getAllSourceFlow().collect { sources ->
                menu.removeGroup(GROUP_ADD_ID)
                sources.forEach { source ->
                    val id = source.id.toInt()
                    menu.add(GROUP_ADD_ID, id, id, source.name)
                }
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val groupId = item.groupId
        if (groupId == GROUP_ADD_ID) {
            val title = item.title
            val id = item.itemId
            mediaModel.loadChannels(id.toLong())
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