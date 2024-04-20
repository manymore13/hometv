package com.wei.liuying.ui.setting

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.preference.PreferenceFragmentCompat
import com.wei.liuying.utils.MMKVDataSource


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
