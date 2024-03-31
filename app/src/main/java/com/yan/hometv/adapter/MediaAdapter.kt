package com.yan.hometv.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yan.hometv.R
import com.yan.hometv.bean.MediaItem

class MediaAdapter(private val dataSet: MutableList<MediaItem>) :
    RecyclerView.Adapter<MediaAdapter.ViewHolder>() {

    var itemClick: ItemClick? = null

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View, adapter: MediaAdapter) : RecyclerView.ViewHolder(view) {
        var mediaIcon: ImageView? = null
        var mediaName: TextView? = null
        var curPos: Int = 0

        init {
            // Define click listener for the ViewHolder's View
            mediaIcon = view.findViewById(R.id.media_icon)
            mediaName = view.findViewById(R.id.media_name)
            itemView.setOnClickListener {
                adapter.itemClick?.onClick(curPos)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.media_item, parent, false)
        return ViewHolder(view, this)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mediaItem = dataSet[position]
        holder.curPos = position
        holder.mediaName?.text = mediaItem.mediaName
        holder.mediaIcon?.let {
            Glide.with(holder.itemView.context).load(mediaItem.mediaUrl).into(it)
        }
    }

    interface ItemClick {
        fun onClick(position: Int)
    }
}