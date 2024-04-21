package com.wei.liuying.ui.mediaplayer

import android.os.Bundle
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.util.UnstableApi
import com.bumptech.glide.Glide
import com.wei.liuying.MediaPlayHelper
import com.wei.liuying.bean.MediaItem
import com.wei.liuying.databinding.MediaPlayerBinding
import com.wei.liuying.receiver.NetWorkStatusReceiver
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MediaPlayerFragment : Fragment() {

    private lateinit var binding: MediaPlayerBinding
    private lateinit var mediaPlayHelper: MediaPlayHelper
    private var mediaItem: MediaItem? = null
    private var rootClick: View.OnClickListener? = null

    companion object {
        const val DELAY = 2000L
        const val TAG = "MediaPlayerFragment"
        const val RECENT_MEDIA = "RECENT_MEDIA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mediaPlayHelper = MediaPlayHelper(requireContext()).apply {
            asyncGetPlayer = { player ->
                binding.playerView.player = player
            }
        }
        this.mediaPlayHelper = mediaPlayHelper
        lifecycle.run {
            addObserver(mediaPlayHelper)
            addObserver(NetWorkStatusReceiver { isConnected ->
                if (isConnected) {
                    mediaPlayHelper.prepare()
                }
            })
        }
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
        binding.playerView.player = mediaPlayHelper.player


        binding.root.setOnClickListener {
            rootClick?.onClick(it)
        }
        if (mediaItem != null) {
            showMediaInfoToast(mediaItem!!)
        }

        return binding.root
    }

    private fun initArguments(){
        arguments?.run {
            val mediaItem = getParcelable<MediaItem>(RECENT_MEDIA)
            if (mediaItem != null) {
                setMediaItem(mediaItem)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mediaPlayHelper.prepare()
        mediaPlayHelper.play()
    }

    override fun onStart() {
        initArguments()
        super.onStart()
    }

    fun setMediaItem(mediaItem: MediaItem) {
        if (::mediaPlayHelper.isInitialized && activity != null) {
            mediaPlayHelper.setMediaItem(mediaItem)
            this.mediaItem = null
            showMediaInfoToast(mediaItem)
        }
    }

    private fun showMediaInfoToast(mediaItem: MediaItem) {
        if (!::binding.isInitialized) {
            return
        }
        TransitionManager.beginDelayedTransition(binding.includeMediaInfo.root)
        binding.includeMediaInfo.run {
            Glide.with(requireActivity()).load(mediaItem.iconUrl).into(mediaIcon)
            mediaName.text = mediaItem.mediaName
            root.isVisible = true
            lifecycleScope.launch {
                delay(DELAY)
                TransitionManager.beginDelayedTransition(binding.includeMediaInfo.root)
                root.isVisible = false
            }
        }
    }

    fun play() {
        if (::mediaPlayHelper.isInitialized) {
            mediaPlayHelper.play()
        }
    }

    fun pause() {
        if (::mediaPlayHelper.isInitialized) {
            mediaPlayHelper.pause()
        }
    }

    fun stop() {
        if (::mediaPlayHelper.isInitialized) {
            mediaPlayHelper.stop()
        }
    }

}