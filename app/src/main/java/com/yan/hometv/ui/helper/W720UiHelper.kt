package com.yan.hometv.ui.helper

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.FragmentActivity
import com.yan.hometv.bean.MediaItem

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
            .show(mediaPlayerFragment)
            .commit()
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        return true
    }

    override fun onClickSelectMediaItem(mediaItem: MediaItem) {
        super.onClickSelectMediaItem(mediaItem)
        TODO("Not yet implemented")
    }

    override fun mediaSourceLoadedComplete() {
        TODO("Not yet implemented")
    }

    override fun onBackReturn(): Boolean {
        TODO("Not yet implemented")
    }

}