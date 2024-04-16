package com.yan.hometv.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.yan.hometv.MediaViewModel
import com.yan.hometv.R
import com.yan.hometv.utils.getIpAddress
import com.yan.hometv.wifi.SimpleWebServer
import com.yan.source.db.Source
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AddSourceFragment : DialogFragment() {

    private var sourceNameEt: EditText? = null

    private var sourceUrlEt: EditText? = null

    private var serviceTv: TextView? = null
        set(value) {
            field = value
            lifecycleScope.launch {
                val ipAddress = getIpAddress(requireContext())
                serviceTv?.text = "当前地址：$ipAddress:8888"
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val simpleWebServer = SimpleWebServer(8888) { sourceName, sourceUrl ->
            sourceNameEt?.setText(sourceName)
            sourceUrlEt?.setText(sourceUrl)
        }
        lifecycle.addObserver(simpleWebServer)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val mediaModel = ViewModelProvider(requireActivity())[MediaViewModel::class.java]

        return activity?.let {
            val builder = AlertDialog.Builder(it).setTitle(R.string.source_add_title)
            // Get the layout inflater.
            val inflater = requireActivity().layoutInflater;

            // Inflate and set the layout for the dialog.
            // Pass null as the parent view because it's going in the dialog
            // layout.
            val v = inflater.inflate(R.layout.dialog_add_source, null)
            val sourceNameEt = v.findViewById<EditText>(R.id.source_name)
            this.sourceNameEt = sourceNameEt
            val sourceUrlEt = v.findViewById<EditText>(R.id.source_url)
            this.sourceUrlEt = sourceUrlEt
            this.serviceTv = v.findViewById<TextView>(R.id.service_info)
            builder.setView(v)
                // Add action buttons.
                .setPositiveButton(
                    R.string.finish
                ) { dialog, _ ->
                    // Sign in the user.
                    val sourceName = sourceNameEt.text.toString()
                    val sourceUrl = sourceUrlEt.text.toString()
                    mediaModel.addNewSource(Source(sourceName, sourceUrl))
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