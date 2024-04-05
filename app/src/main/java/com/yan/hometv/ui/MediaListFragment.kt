package com.yan.hometv.ui

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
import com.bumptech.glide.Glide
import com.yan.hometv.MediaPlayHelper
import com.yan.hometv.MediaViewModel
import com.yan.hometv.R
import com.yan.hometv.adapter.MediaAdapter
import com.yan.hometv.bean.MediaItem
import com.yan.hometv.bean.toMediaItem
import com.yan.hometv.databinding.MediaListBinding


/**
 * 节目列表
 */
class MediaListFragment : Fragment() {

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
        mediaPlayHelper = MediaPlayHelper(requireContext())
            .asyncGetPlayer { player ->
                onGetPlayer(player)
                mediaPlayHelper.setPlayerListener(MediaListener())
            }
    }


    private fun onGetPlayer(player: Player) {
        player.addListener(mediaListener)
        player.currentMediaItem?.toMediaItem()?.run {
            showMediaInfo(this)
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
            mediaPlayHelper.pause()
            PlayerActivity.start(requireActivity(), mediaItem)
        }
        val mediaModel = ViewModelProvider(requireActivity())[MediaViewModel::class.java]
        mediaModel.complete.observe(requireActivity()) {
            val mediaAdapter = MediaAdapter(mediaModel.mediaList)
            mediaAdapter.itemClick = object : MediaAdapter.ItemClick {
                override fun onClick(position: Int) {
                    mediaModel.selectMediaLiveData.value = mediaModel.mediaList[position]
                    itemSelected(mediaModel.mediaList[position])
                }
            }
            binding.mediaRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            binding.mediaRecyclerView.adapter = mediaAdapter
        }
        return binding.root
    }

    var mediaItem: MediaItem? = null

    private fun itemSelected(mediaItem: MediaItem) {
        this.mediaItem = mediaItem
        binding.mediaPlayInfo?.tvMediaInfo?.text = ""
        showMediaInfo(mediaItem)
        mediaPlayHelper.setMediaItem(mediaItem)
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
                        tvMediaInfo.text = mediaPlayHelper.getMediaDebugInfo()
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