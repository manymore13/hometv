package com.wei.liuying.utils

import android.transition.TransitionManager
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun playAnim(animRootView: ViewGroup, block: () -> Unit) {
    TransitionManager.beginDelayedTransition(animRootView)
    block()
}

fun showToastAnim(
    lifecycleOwner: LifecycleOwner,
    animRootView: ViewGroup,
    delay: Long = 2000,
    block: () -> Unit
) {
    TransitionManager.beginDelayedTransition(animRootView)
}

fun delayHideAnim(lifecycleOwner: LifecycleOwner, animRootView: ViewGroup, delay: Long = 2000) {
    lifecycleOwner.lifecycleScope.launch {
        delay(delay)
        playAnim(animRootView) {
            animRootView.isVisible = false
        }
    }
}