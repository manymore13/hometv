package com.yan.hometv.ui.helper

import android.os.Bundle
import android.transition.TransitionManager
import android.view.KeyEvent
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import com.yan.hometv.R
import com.yan.hometv.bean.MediaItem
import com.yan.hometv.utils.hideFragment
import com.yan.hometv.utils.showFragment

/**
 * @author manymore13
 * @Description W720 设备
 * @date 2024/4/2
 */
class W720UiHelper(activity: FragmentActivity, mediaPlayResId: Int, mediaListResId: Int) :
    MediaUiHelper(activity, mediaPlayResId, mediaListResId) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity.supportFragmentManager.beginTransaction()
            .hide(mediaListFragment)
            .commit()

//        mediaPlayerFragment.rootClick = View.OnClickListener {
//
//            TransitionManager.beginDelayedTransition(binding.mediaList)
//
//            binding.mediaList.isVisible = !binding.mediaList.isVisible
//
//            showFragment(activity.supportFragmentManager, R.id.media_list, mediaListFragment)
//        }
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        return true
    }

    override fun onClickSelectMediaItem(mediaItem: MediaItem) {
        super.onClickSelectMediaItem(mediaItem)
        hideFragment(activity.supportFragmentManager, mediaListFragment)
    }

    override fun mediaSourceLoadedComplete() {
        if (mediaModel.mediaList.size > 0) {
//            mediaPlayerFragment.setMediaItem(mediaModel.mediaList[0])
        }
    }

    override fun onBackReturn(): Boolean {
        return false
    }

}