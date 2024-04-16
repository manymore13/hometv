package com.yan.hometv.ui.medialist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.Player
import androidx.media3.common.Player.Listener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.bumptech.glide.Glide
import com.yan.hometv.MediaPlayHelper
import com.yan.hometv.MediaViewModel
import com.yan.hometv.R
import com.yan.hometv.adapter.MediaAdapter
import com.yan.hometv.bean.MediaItem
import com.yan.hometv.bean.toMediaItem
import com.yan.hometv.databinding.MediaListBinding
import com.yan.hometv.ui.mediaplayer.PlayerActivity
import com.yan.hometv.utils.toast


/**
 * 节目列表
 */
open class MediaListFragment : Fragment() {

    private lateinit var mediaPlayHelper: MediaPlayHelper
    private lateinit var binding: MediaListBinding
    private val mediaListener by lazy {
        MediaListener()
    }

    companion object {
        const val TAG = "MediaListFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mediaPlayHelper = MediaPlayHelper(requireContext()).apply {
            asyncGetPlayer = { player ->
                onGetPlayer(player)
                mediaPlayHelper.setPlayerListener(MediaListener())
            }
        }
        lifecycle.addObserver(mediaPlayHelper)
    }

    private fun onGetPlayer(player: Player) {
        player.addListener(mediaListener)
        player.currentMediaItem?.toMediaItem()?.run {
            showMediaInfo(this, player.isPlaying)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding = MediaListBinding.inflate(inflater, container, false)
        this.binding = binding
        binding.mediaPlayInfo?.ivPlayState?.setOnClickListener {
            if (mediaPlayHelper.player?.isPlaying == true) {
                mediaPlayHelper.pause()
            } else {
                mediaPlayHelper.play()
            }
        }
        binding.mediaPlayInfo?.root?.setOnClickListener {
            if (mediaPlayHelper.isSupportVideo()) {
                PlayerActivity.start(requireActivity(), mediaItem)
            } else {
                toast(getString(R.string.not_support_video_warn))
            }
        }
        val mediaModel = ViewModelProvider(requireActivity())[MediaViewModel::class.java]
        mediaModel.complete.observe(viewLifecycleOwner) {
            val mediaAdapter = MediaAdapter(mediaModel.mediaList)
            mediaAdapter.itemClick = object : MediaAdapter.ItemClick {
                override fun onClick(position: Int) {
                    mediaModel.selectMediaItem(position)
                }
            }
            binding.mediaRecyclerView.layoutManager = getLayoutManager(requireContext())
            binding.mediaRecyclerView.adapter = mediaAdapter
        }
        mediaModel.selectMediaLiveData.observe(viewLifecycleOwner) {
            itemSelected(it)
        }
        return binding.root
    }

    protected open fun getLayoutManager(context: Context): LayoutManager {
        return LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    var mediaItem: MediaItem? = null

    private fun itemSelected(mediaItem: MediaItem) {
        this.mediaItem = mediaItem
        if (binding.mediaPlayInfo != null) {
            binding.mediaPlayInfo?.tvMediaInfo?.text = ""
            showMediaInfo(mediaItem)
            mediaPlayHelper.setMediaItem(mediaItem)
        }
    }

    private fun showMediaInfo(mediaItem: MediaItem, isPlaying: Boolean = false) {
        binding.mediaPlayInfo?.run {
            mediaName.text = mediaItem.mediaName
            Glide.with(requireContext()).load(mediaItem.iconUrl).into(mediaIcon.mediaIcon)
            updateStateUi(isPlaying)
        }
    }

    private fun updateStateUi(isPlaying: Boolean) {
        binding.mediaPlayInfo?.run {
            val resId =
                if (isPlaying) androidx.media3.ui.R.drawable.exo_icon_pause else androidx.media3.ui.R.drawable.exo_icon_play
            ivPlayState.setImageResource(resId)
        }
    }

    inner class MediaListener : Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
            updateStateUi(isPlaying)
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
            when (playbackState) {
                Player.STATE_READY -> {
                    binding.mediaPlayInfo?.run {
                        binding.mediaPlayInfo?.tvPlayState?.isVisible = false
                        tvMediaInfo.text = mediaPlayHelper.getMediaTypeInfo()
                    }
                }

                Player.STATE_BUFFERING -> {
                    binding.mediaPlayInfo?.run {
                        tvPlayState.isVisible = true
                        tvPlayState.setText(R.string.state_buffering)
                    }
                }

                else -> {
                    binding.mediaPlayInfo?.tvPlayState?.isVisible = false
                }
            }
        }
    }
}