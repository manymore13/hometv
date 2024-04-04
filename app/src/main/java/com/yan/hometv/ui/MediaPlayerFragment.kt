package com.yan.hometv.ui

import android.content.ComponentName
import android.os.Bundle
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.bumptech.glide.Glide
import com.google.common.util.concurrent.MoreExecutors
import com.yan.hometv.MediaPlayHelper
import com.yan.hometv.bean.MediaItem
import com.yan.hometv.databinding.MediaPlayerBinding
import com.yan.hometv.receiver.NetWorkStatusReceiver
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MediaPlayerFragment : Fragment() {

    private lateinit var binding: MediaPlayerBinding
    private var mediaPlayHelper: MediaPlayHelper? = null
    private var mediaItem: MediaItem? = null
    var rootClick: View.OnClickListener? = null

    companion object {
        const val delay = 1500L
        const val TAG = "MediaPlayerFragment"
    }

    @UnstableApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MediaPlayerBinding.inflate(inflater, container, false)
        binding.playerView.run {
            useController = false
        }

        binding.root.setOnClickListener {
            rootClick?.onClick(it)
        }
        lifecycle.addObserver(NetWorkStatusReceiver { isConnected ->
            if (isConnected) {
                mediaPlayHelper?.prepare()
            }
        })
        if (mediaItem != null) {
            showMediaInfo(mediaItem!!)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (view?.isVisible == true) {
            mediaPlayHelper?.play()
        }
    }

    override fun onStart() {
        super.onStart()
        val context = requireContext()
        val sessionToken =
            SessionToken(context, ComponentName(context, MediaPlayService::class.java))
        val controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        controllerFuture.addListener(
            {
                // Call controllerFuture.get() to retrieve the MediaController.
                // MediaController implements the Player interface, so it can be
                // attached to the PlayerView UI component.
                val player = controllerFuture.get()
                binding.playerView.player = player
                mediaPlayHelper = getVideoPlayHelper(player)
                lifecycle.addObserver(mediaPlayHelper!!)
                mediaPlayHelper?.setMediaItem(mediaItem)
            },
            MoreExecutors.directExecutor()
        )
    }

    fun setMediaItem(mediaItem: MediaItem) {
        if (mediaPlayHelper != null) {
            mediaPlayHelper!!.setMediaItem(mediaItem)
            this.mediaItem = null
        } else {
            this.mediaItem = mediaItem
        }
        showMediaInfo(mediaItem)
    }

    private fun showMediaInfo(mediaItem: MediaItem) {
        if (!::binding.isInitialized) {
            return
        }
        TransitionManager.beginDelayedTransition(binding.includeMediaInfo.root)
        binding.includeMediaInfo.run {
            Glide.with(requireActivity()).load(mediaItem.iconUrl).into(mediaIcon)
            mediaName.text = mediaItem.mediaName
            root.isVisible = true
            lifecycleScope.launch {
                delay(delay)
                TransitionManager.beginDelayedTransition(binding.includeMediaInfo.root)
                root.isVisible = false
            }
        }
    }

    fun play() {
        mediaPlayHelper?.play()
    }

    fun pause() {
        mediaPlayHelper?.pause()
    }

    private fun getVideoPlayHelper(player: Player): MediaPlayHelper {
        return MediaPlayHelper(requireContext(), player)
    }

}