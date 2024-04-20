package com.wei.liuying.ui.setting

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.preference.ListPreference
import androidx.preference.Preference
import com.wei.liuying.MediaViewModel
import com.wei.liuying.R
import com.wei.liuying.ui.AddSourceFragment
import com.wei.liuying.ui.SourceEditFragment
import com.wei.liuying.ui.SourceEditFragment.Companion.SOURCE_LIST
import com.wei.liuying.utils.SOURCE_UPDATE_TIME_KEY
import com.wei.liuying.utils.getAppSharedPreferences
import com.wei.source.db.Source
import com.wei.source.utils.kv
import kotlinx.coroutines.launch

/**
 * @author manymore13
 * @Description SettingsFragment
 * @date 2024/4/9
 */
class SettingsFragment : BasePreferenceFragment() {

    companion object {
        const val TAG = "SettingsFragment"
    }

    val preferences = getAppSharedPreferences()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)
        setPreferencesFromResource(R.xml.preferences, rootKey)

        initCurrentSource()
        initAddSource()
        initDeleteSource()
    }

    private fun initCurrentSource() {

        val names = resources.getStringArray(R.array.entries_source_update_time_array)
        val values = resources.getStringArray(R.array.entry_source_update_time_values_array)
        val updateTime = preferences.getLong(SOURCE_UPDATE_TIME_KEY, 86400000L)
        val index = values.indexOf(updateTime.toString())
        val preference = preferenceManager.findPreference<ListPreference>(SOURCE_UPDATE_TIME_KEY)
        preference?.summary = names[index]
        preference?.setOnPreferenceChangeListener { _, newValue ->
            Log.d(TAG, "onPreferenceChange: $newValue")
            val index = values.indexOf(newValue as String)
            preference.summary = names[index]
            kv.encode(SOURCE_UPDATE_TIME_KEY, newValue.toLong())
            return@setOnPreferenceChangeListener false
        }
    }

    private fun initDeleteSource() {

        val preference =
            preferenceManager.findPreference<Preference>(getString(R.string.source_delete_key))
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

    private fun initAddSource() {

        val preference =
            preferenceManager.findPreference<Preference>(getString(R.string.source_add_key))
        preference?.setOnPreferenceClickListener {
            AddSourceFragment().show(childFragmentManager, "add_source")
            true
        }

    }

}