package com.yan.hometv.ui

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.yan.hometv.MediaViewModel
import com.yan.source.db.Source

/**
 * @author manymore13
 * @Description SourceEditFragment
 * @date 2024/4/16
 */
class SourceEditFragment : DialogFragment() {

    companion object {
        const val SOURCE_LIST = "source_list"
    }

    private val sourceList: MutableList<Source> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            sourceList.clear()
            it.getParcelableArrayList<Source>(SOURCE_LIST)?.let { sourceList.addAll(it) }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val nameList = Array<CharSequence>(sourceList.size) {
            sourceList.get(it).name
        }

        val checkedItems = BooleanArray(sourceList.size)
        return AlertDialog.Builder(requireActivity()).setTitle("Select items")
            .setMultiChoiceItems(nameList, checkedItems) { _, which, isChecked ->
                checkedItems[which] = isChecked
            }.setPositiveButton("OK") { _, _ ->
                // Handle the selected items
                val mediaModel = ViewModelProvider(requireActivity())[MediaViewModel::class.java]
                val tsourceList = mutableListOf<Source>()
                checkedItems.forEachIndexed { index, isChecked ->
                    if (isChecked) {
                        tsourceList.add(sourceList[index])
                    }
                }
                mediaModel.deleteSources(tsourceList)

            }.setNegativeButton("Cancel") { _, _ ->
                // Cancel the dialog
                dismiss()
            }.create()
    }
}