@file:JvmName("PreferencesSource")
package com.yan.hometv.utils

import android.content.SharedPreferences
import androidx.preference.PreferenceDataStore
import com.tencent.mmkv.MMKV

/**
 * @author manymore13
 * @Description MMKV DataSource
 * @date 2024/4/9
 */
class MMKVDataSource : PreferenceDataStore() {

    private val mmkv by lazy {
        MMKV.defaultMMKV()
    }


    override fun putString(key: String, value: String?) {
        mmkv.encode(key, value)
    }

    override fun putStringSet(key: String, values: MutableSet<String>?) {
        mmkv.encode(key, values)
    }

    override fun putInt(key: String, value: Int) {
        mmkv.encode(key, value)
    }

    override fun putLong(key: String, value: Long) {
        mmkv.encode(key, value)
    }

    override fun putFloat(key: String, value: Float) {
        mmkv.encode(key, value)
    }

    override fun putBoolean(key: String, value: Boolean) {
        mmkv.encode(key, value)
    }

    override fun getString(key: String, defValue: String?): String? {
        return mmkv.decodeString(key, defValue)
    }

    override fun getStringSet(key: String, defValues: MutableSet<String>?): MutableSet<String>? {
        return mmkv.decodeStringSet(key, defValues)
    }

    override fun getInt(key: String, defValue: Int): Int {
        return mmkv.decodeInt(key, defValue)
    }

    override fun getLong(key: String, defValue: Long): Long {
        return mmkv.decodeLong(key, defValue)
    }

    override fun getFloat(key: String, defValue: Float): Float {
        return mmkv.decodeFloat(key, defValue)
    }

    override fun getBoolean(key: String, defValue: Boolean): Boolean {
        return mmkv.decodeBool(key, defValue)
    }
}

fun getAppSharedPreferences(): SharedPreferences {
    return MMKV.defaultMMKV()
}