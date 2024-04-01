package com.yan.hometv.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.yan.hometv.MediaViewModel
import com.yan.hometv.adapter.MediaAdapter
import com.yan.hometv.databinding.MediaListBinding


/**
 * 节目列表
 */
class MediaListFragment : Fragment() {

    private var mediaModel: MediaViewModel? = null
    companion object{
        const val TAG = "MediaListFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = MediaListBinding.inflate(inflater, container, false)
        mediaModel = ViewModelProvider(requireActivity())[MediaViewModel::class.java]
        mediaModel?.init()
        mediaModel?.complete?.observe(requireActivity()) {
            val mediaAdater = MediaAdapter(mediaModel!!.mediaList)
            mediaAdater.itemClick = object : MediaAdapter.ItemClick {
                override fun onClick(position: Int) {
                    mediaModel?.selectMediaLiveData?.value = mediaModel!!.mediaList[position]
                }

            }
            binding.mediaRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            binding.mediaRecyclerView.adapter = mediaAdater
        }
        return binding.root
    }
}