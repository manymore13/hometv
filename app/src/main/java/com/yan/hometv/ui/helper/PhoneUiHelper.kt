package com.yan.hometv.ui.helper

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.FragmentActivity
import com.yan.hometv.R
import com.yan.hometv.bean.MediaItem
import com.yan.hometv.ui.MediaListFragment
import com.yan.hometv.ui.MediaPlayerFragment
import com.yan.hometv.utils.hideFragment
import com.yan.hometv.utils.showFragment

/**
 * @author manymore13
 * @Description 手机设备
 * @date 2024/4/2
 */
class PhoneUiHelper(activity: FragmentActivity, mediaPlayResId: Int, mediaListResId: Int) :
    MediaUiHelper(activity, mediaPlayResId, mediaListResId) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fragmentManager = activity.supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(mediaListResId, mediaListFragment, MediaListFragment.TAG)
            .commit()
        mediaModel.init()
        hideFragment(activity.supportFragmentManager, mediaPlayerFragment)
    }

    override fun onClickSelectMediaItem(mediaItem: MediaItem) {
        super.onClickSelectMediaItem(mediaItem)
        val fragmentManager = activity.supportFragmentManager
        showFragment(fragmentManager, R.id.media_play, mediaPlayerFragment)
    }

    override fun mediaSourceLoadedComplete() {
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        return true
    }

    override fun onBackReturn(): Boolean {
        val fragmentManager = activity.supportFragmentManager
        val stackCount = fragmentManager.backStackEntryCount
        for (i in 0..stackCount - 1) {
            val stack = fragmentManager.getBackStackEntryAt(i)
            if (stack.name == MediaPlayerFragment.TAG) {
                mediaPlayerFragment.pause()
                break
            }
        }
        return false
    }
}