package com.yan.hometv.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.yan.hometv.MediaViewModel
import com.yan.hometv.R
import com.yan.hometv.databinding.ActivityTvMainBinding

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
        supportFragmentManager.beginTransaction()
            .replace(R.id.media_list, mediaListFragment, mediaListFragment::class.simpleName)
            .commit()
        mediaModel.init()

        mediaModel.showLoading.observe(this) { show ->
            showLoading(show)
        }

        mediaModel.selectMediaLiveData.observe(this) { mediaItem ->
            mediaPlayerFragment.arguments = Bundle().apply {
                putParcelable(PlayerActivity.MEDIA_ITEM, mediaItem)
            }
            supportFragmentManager.beginTransaction()
                .remove(mediaListFragment)
                .replace(R.id.media_play, mediaPlayerFragment)
                .addToBackStack("")
                .commit()
        }
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_MENU || keyCode == KeyEvent.KEYCODE_TV_CONTENTS_MENU) {
            supportFragmentManager.popBackStack()
            supportFragmentManager.beginTransaction()
                .replace(R.id.media_list, mediaListFragment)
                .commit()
            return true
        }
        return super.onKeyUp(keyCode, event)
    }

    override fun onStop() {
        super.onStop()
        mediaPlayerFragment.pause()
    }

    private fun showLoading(showLoading: Boolean) {
        binding.load.isVisible = showLoading
    }

    class TvMediaListFragment : MediaListFragment() {
        override fun getLayoutManager(context: Context): RecyclerView.LayoutManager {
//            return FlexboxLayoutManager(context).apply {
//                flexDirection = FlexDirection.ROW
//                flexWrap = FlexWrap.WRAP
//            }
            return GridLayoutManager(context, 6, RecyclerView.VERTICAL, false)
        }
    }
}