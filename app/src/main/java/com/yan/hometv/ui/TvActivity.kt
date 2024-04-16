package com.yan.hometv.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yan.hometv.MediaViewModel
import com.yan.hometv.R
import com.yan.hometv.bean.MediaItem
import com.yan.hometv.databinding.ActivityTvMainBinding
import com.yan.hometv.ui.medialist.MediaListFragment
import com.yan.hometv.ui.mediaplayer.MediaPlayReceiver
import com.yan.hometv.ui.mediaplayer.MediaPlayerFragment
import com.yan.hometv.ui.mediaplayer.PlayerActivity
import com.yan.hometv.ui.setting.SettingActivity
import com.yan.source.utils.kv
import kotlinx.coroutines.launch

/**
 * @author manymore13
 * @Description tv ui
 * @date 2024/4/3
 */
class TvActivity : AppCompatActivity() {

    companion object {
        const val GROUP_ADD_ID = 520

        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, TvActivity::class.java)
            context.startActivity(starter)
        }
    }

    private val mediaListFragment = TvMediaListFragment()
    private val mediaPlayerFragment = MediaPlayerFragment()


    private lateinit var binding: ActivityTvMainBinding

    private val mediaModel by lazy {
        ViewModelProvider(this)[MediaViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTvMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recentMediaItem = mediaModel.getRecentMediaItem()
        if (recentMediaItem != null) {
            mediaPlayerFragment.arguments = Bundle().apply {
                putParcelable(PlayerActivity.RECENT_MEDIA, recentMediaItem)
            }
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.media_play, mediaPlayerFragment, mediaPlayerFragment::class.simpleName)
            .commit()
        mediaModel.initSource()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        mediaModel.showLoading.observe(this) { show ->
            showLoading(show)
        }
        mediaModel.complete.observe(this) {
            lifecycleScope.launch {
                val channelId = kv.decodeLong(MediaViewModel.RECENT_MEDIA, 0)
                val mediaItem = mediaModel.getMediaItemByChannelId(channelId)
                if (mediaItem != null) {
                    playMediaItem(mediaItem)
                } else {
                    mediaModel.selectMediaItem(0)
                }
            }

        }

        mediaModel.selectMediaLiveData.observe(this) {
            playMediaItem(it)
        }
    }

    private fun playMediaItem(mediaItem: MediaItem) {
        mediaPlayerFragment.arguments = Bundle().apply {
            putParcelable(PlayerActivity.RECENT_MEDIA, mediaItem)
        }
        mediaPlayerFragment.setMediaItem(mediaItem)
        showHideMediaListFragment(false)
    }

    private fun showHideMediaListFragment(show: Boolean) {
        if (show) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.media_list, mediaListFragment)
                .setCustomAnimations(
                    R.anim.left_in,
                    R.anim.right_out,
                    R.anim.left_in,
                    R.anim.right_out
                )
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .remove(mediaListFragment)
                .setCustomAnimations(
                    R.anim.left_in,
                    R.anim.right_out,
                    R.anim.left_in,
                    R.anim.right_out
                )
                .commit()
        }

    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_MENU -> {
                SettingActivity.start(this)
                return true
            }

            KeyEvent.KEYCODE_ENTER, KeyEvent.KEYCODE_TV_CONTENTS_MENU -> {
                if (mediaPlayerFragment.isAdded && !mediaListFragment.isAdded) {
                    showHideMediaListFragment(true)
                } else {
                    showHideMediaListFragment(false)
                }
                return true
            }

            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                // 菜单
                if (mediaPlayerFragment.isAdded && !mediaListFragment.isAdded) {
                    showHideMediaListFragment(true)
                    return true
                }
            }

            KeyEvent.KEYCODE_BACK -> {
                if (mediaListFragment.isAdded) {
                    showHideMediaListFragment(false)
                    return true
                }
            }

            KeyEvent.KEYCODE_CHANNEL_UP, KeyEvent.KEYCODE_DPAD_UP -> {
                if (!mediaListFragment.isAdded) {
                    mediaModel.prev()
                    return true
                }
            }

            KeyEvent.KEYCODE_CHANNEL_DOWN, KeyEvent.KEYCODE_DPAD_DOWN -> {
                if (!mediaListFragment.isAdded) {
                    mediaModel.next()
                    return true
                }
            }
        }
        return super.onKeyUp(keyCode, event)
    }


    override fun onPause() {
        super.onPause()
        mediaPlayerFragment.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        sendBroadcast(Intent(MediaPlayReceiver.ACTION_DESTROY_EVENT))
    }

    private fun showLoading(showLoading: Boolean) {
        binding.load.isVisible = showLoading
    }

    class TvMediaListFragment : MediaListFragment() {
        override fun getLayoutManager(context: Context): RecyclerView.LayoutManager {
            return GridLayoutManager(context, 3, RecyclerView.VERTICAL, false)
        }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            val view = super.onCreateView(inflater, container, savedInstanceState)
            view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.media_list_bg))
            return view
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onResume() {
            super.onResume()
            view?.isFocusable = true
            view?.setFocusable(View.FOCUSABLE)
        }
    }
}