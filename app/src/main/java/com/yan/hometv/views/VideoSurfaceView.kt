package com.yan.hometv.views

import android.content.Context
import android.util.AttributeSet
import android.view.SurfaceView
import androidx.media3.common.Player
import androidx.media3.common.Player.Listener

class VideoSurfaceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SurfaceView(context, attrs, defStyleAttr) {

    private var player: Player? = null

    fun setPlayer(player: Player) {
        this.player = player
        player.addListener(object : Listener {

            override fun onRenderedFirstFrame() {
                // 当第一帧渲染完成时调用
            }


        })
        player.setVideoSurfaceView(this)
    }
}