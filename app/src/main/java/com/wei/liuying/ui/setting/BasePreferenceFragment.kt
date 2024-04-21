package com.wei.liuying.ui.setting

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.wei.liuying.utils.MMKVDataSource
import com.wei.liuying.utils.getAppSharedPreferences


/**
 * @author manymore13
 * @Description PreferenceFragment 使用的是mmkv源
 * @date 2024/4/9
 */
open class BasePreferenceFragment : PreferenceFragmentCompat() {

    protected val preferences = getAppSharedPreferences()

    fun <T : Preference?> findPreference(@StringRes keyId: Int): T? {
        return if (preferenceManager == null) {
            null
        } else preferenceManager.findPreference(getString(keyId))
    }

    @CallSuper
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.preferenceDataStore = MMKVDataSource()
    }
}
