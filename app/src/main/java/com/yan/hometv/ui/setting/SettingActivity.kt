package com.yan.hometv.ui.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yan.hometv.R

/**
 * @author manymore13
 * @Description SettingActivity
 * @date 2024/4/9
 */
class SettingActivity : AppCompatActivity() {

    companion object {
        const val TAG = "SettingActivity"
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, SettingActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings_container, SettingsFragment())
            .commit()
    }
}