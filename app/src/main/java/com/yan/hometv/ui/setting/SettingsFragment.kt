package com.yan.hometv.ui.setting

import android.os.Bundle
import android.util.Log
import androidx.preference.ListPreference
import com.yan.hometv.R
import com.yan.hometv.utils.getAppSharedPreferences

/**
 * @author manymore13
 * @Description SettingsFragment
 * @date 2024/4/9
 */
class SettingsFragment : BasePreferenceFragment() {

    companion object {
        const val TAG = "SettingsFragment"
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)
        setPreferencesFromResource(R.xml.preferences, rootKey)

        initCurrentSource()
    }

    private fun initCurrentSource() {
        val preferences = getAppSharedPreferences()

        val key = getString(R.string.media_source_key)
        val currentSource = preferences.getString(key, "") ?: ""
        val preference = preferenceManager.findPreference<ListPreference>(key)
        preference?.summary = currentSource
        preference?.setOnPreferenceChangeListener { _, newValue ->
            Log.d(TAG, "onPreferenceChange: $newValue")
            preference.summary = newValue as String
            return@setOnPreferenceChangeListener true
        }
    }
}