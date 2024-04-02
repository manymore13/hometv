package com.yan.hometv.ui.helper

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.FragmentActivity
import com.yan.hometv.bean.MediaItem
import com.yan.hometv.utils.isW720

/**
 * @author manymore13
 * @Description 设备UI基类
 * @date 2024/4/2
 */
interface IMediaDeviceUi {
    /**
     * 初始化
     */
    fun onCreate(savedInstanceState: Bundle?)

    /**
     * 从菜单点击选择一个media
     */
    fun onClickSelectMediaItem(mediaItem: MediaItem)

    fun mediaSourceLoadedComplete()

    fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean

    /**
     * 点击返回键
     */
    fun onBackReturn():Boolean

    companion object {
        fun getMediaDevice(
            activity: FragmentActivity,
            mediaPlayResId: Int, mediaListResId: Int
        ): IMediaDeviceUi {
            return if (isW720()) {
                W720UiHelper(activity, mediaPlayResId, mediaListResId)
            } else {
                PhoneUiHelper(activity, mediaPlayResId, mediaListResId)
            }
        }
    }
}