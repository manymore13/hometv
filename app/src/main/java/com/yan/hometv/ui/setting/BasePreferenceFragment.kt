package com.yan.hometv.ui.setting

import android.content.SharedPreferences
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.tencent.mmkv.MMKV
import com.yan.hometv.utils.MMKVDataSource


/**
 * @author manymore13
 * @Description PreferenceFragment
 * @date 2024/4/9
 */
open class BasePreferenceFragment : PreferenceFragmentCompat() {

    @CallSuper
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.preferenceDataStore = MMKVDataSource()
    }
}
