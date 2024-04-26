package com.wei.liuying.ui.setting

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.preference.ListPreference
import androidx.preference.Preference
import com.wei.liuying.MediaViewModel
import com.wei.liuying.R
import com.wei.liuying.ui.AddSourceFragment
import com.wei.liuying.ui.SourceEditFragment
import com.wei.liuying.ui.SourceEditFragment.Companion.SOURCE_LIST
import com.wei.liuying.utils.AppConfig
import com.wei.liuying.utils.SOURCE_UPDATE_TIME_KEY
import com.wei.liuying.utils.toast
import com.wei.source.SourceRepository
import com.wei.source.db.Source
import kotlinx.coroutines.launch

/**
 * @author manymore13
 * @Description SettingsFragment
 * @date 2024/4/9
 */
class SettingsFragment : BasePreferenceFragment() {

    private val sourceRepository by lazy {
        SourceRepository(requireContext())
    }

    private val loadingDialog by lazy {
        ProgressDialog(context).apply {
            setMessage(getString(R.string.updating))
        }
    }


    companion object {
        const val TAG = "SettingsFragment"
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)
        setPreferencesFromResource(R.xml.preferences, rootKey)

        initHowLongRefreshSource()
        initRefreshSelectSource()
        initSelectSource()
        initAddSource()
        initDeleteSource()
    }

    private fun initRefreshSelectSource() {
        val preference = findPreference<Preference>(R.string.source_refresh_key)
        preference?.setOnPreferenceClickListener {
            loadingDialog.show()
            lifecycleScope.launch {
                val sourceId = AppConfig.getSelectedSourceId()
                Log.d(TAG, "sourceId = $sourceId")
                val source = sourceRepository.getSourceById(sourceId)
                if (source != null) {
                    try {
                        sourceRepository.deleteChannelBySourceId(sourceId)
                        sourceRepository.manageSourceRefresh(source, true)
                        toast(getString(R.string.update_success))
                    } catch (e: Exception) {
                        toast(e.message)
                    } finally {
                        loadingDialog.dismiss()
                    }
                } else {
                    toast(getString(R.string.refresh_no_select_source_tip))
                }
            }
            true
        }
    }

    private fun updateSourceList(sourceList: MutableList<Source>, preference: ListPreference?) {
        val nameList = mutableListOf<String>()
        val idList = mutableListOf<CharSequence>()
        sourceList.forEach { source ->
            nameList.add(source.name)
            idList.add(source.id.toString())
        }
        preference?.run {
            entries = nameList.toTypedArray()
            entryValues = idList.toTypedArray()
        }
        val selectedSourceId = AppConfig.getSelectedSourceId()
        val index = idList.indexOf(selectedSourceId.toString())
        if (index != -1) {
            preference?.setValueIndex(index)
            preference?.summary =
                nameList[index] + getString(R.string.source_select_summary)
        }

        preference?.setOnPreferenceChangeListener { _, newValue ->
            val newV = newValue as String
            AppConfig.setSelectedSourceId(newV.toLong())
            Log.d(TAG, "onPreferenceChange: $newValue")
            val index = idList.indexOf(newValue)
            preference.summary =
                nameList[index] + getString(R.string.source_select_summary)

            LocalBroadcastManager.getInstance(requireContext())
                .sendBroadcast(Intent(MediaViewModel.ACTION_CHANGE_SOURCE))
            false
        }
    }

    private fun initSelectSource() {
        val preference = findPreference<ListPreference>(R.string.source_select_key)
        lifecycleScope.launch {
            val sourceList = sourceRepository.getAllSource()
            updateSourceList(sourceList, preference)

            preference?.setOnPreferenceClickListener {
                updateSourceList(sourceList, preference)
                true
            }
        }

    }

    private fun initHowLongRefreshSource() {
        val names = resources.getStringArray(R.array.entries_source_update_time_array)
        val values = resources.getStringArray(R.array.entry_source_update_time_values_array)
        val updateTime = AppConfig.getSourceUpdateCycleTime()
        val index = values.indexOf(updateTime.toString())
        val preference = findPreference<ListPreference>(SOURCE_UPDATE_TIME_KEY)
        if (index != -1) {
            preference?.setValueIndex(index)
            preference?.summary = names[index]
        }
        preference?.setOnPreferenceChangeListener { _, newValue ->
            Log.d(TAG, "onPreferenceChange: $newValue")
            val index = values.indexOf(newValue as String)
            preference.summary = names[index]

            return@setOnPreferenceChangeListener true
        }
    }

    /**
     * 删除源
     */
    private fun initDeleteSource() {

        val preference = findPreference<Preference>(getString(R.string.source_delete_key))
        preference?.setOnPreferenceClickListener {
            val mediaModel = ViewModelProvider(requireActivity())[MediaViewModel::class.java]

            lifecycleScope.launch {
                val sourceEditFragment = SourceEditFragment()
                sourceEditFragment.arguments = Bundle().apply {

                    val sourceList = ArrayList<Source>()
                    mediaModel.getAllSource().forEach {
                        sourceList.add(it)
                    }
                    putParcelableArrayList(SOURCE_LIST, sourceList)
                }
                sourceEditFragment.show(childFragmentManager, "SourceEditFragment")
            }

            true
        }

    }

    /**
     * 添加源
     */
    private fun initAddSource() {

        val preference = findPreference<Preference>(getString(R.string.source_add_key))
        preference?.setOnPreferenceClickListener {
            AddSourceFragment().show(childFragmentManager, "add_source")
            true
        }

    }

}