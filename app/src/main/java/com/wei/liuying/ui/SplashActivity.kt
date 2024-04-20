package com.wei.liuying.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.wei.liuying.databinding.ActivitySplashBinding
import com.wei.liuying.utils.getVersionName
import com.wei.liuying.utils.isW720
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @author zhiwei.Yan
 * @Description 启动页面
 * @date 2024/4/2
 */
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binder = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binder.root)
        binder.tvVersion.text = getVersionName()
        lifecycleScope.launch {
            delay(1500)
            if (isW720()) {
                TvActivity.start(this@SplashActivity)
            } else {
                MainActivity.start(this@SplashActivity)
            }
            finish()
        }

    }
}