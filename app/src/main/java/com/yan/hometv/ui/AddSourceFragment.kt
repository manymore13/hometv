package com.yan.hometv.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.yan.hometv.MediaViewModel
import com.yan.hometv.R
import com.yan.source.utils.MediaSource

class AddSourceFragment : DialogFragment() {

//    private var mediaModel: MediaViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        mediaModel = ViewModelProvider(requireActivity())[MediaViewModel::class.java]
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val mediaModel = ViewModelProvider(requireActivity())[MediaViewModel::class.java]


        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater.
            val inflater = requireActivity().layoutInflater;

            // Inflate and set the layout for the dialog.
            // Pass null as the parent view because it's going in the dialog
            // layout.
            val v = inflater.inflate(R.layout.dialog_add_source, null)
            val sourceNameEt = v.findViewById<EditText>(R.id.source_name)
            val sourceUrlEt = v.findViewById<EditText>(R.id.source_url)
            builder.setView(v)
                // Add action buttons.
                .setPositiveButton(
                    R.string.finish
                ) { dialog, _ ->
                    // Sign in the user.
                    val sourceName = sourceNameEt.text.toString()
                    val sourceUrl = sourceUrlEt.text.toString()
                    mediaModel.addNewSource(MediaSource(sourceName, sourceUrl))
                }
                .setNegativeButton(
                    R.string.cancel
                ) { dialog, _ ->
                    dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}