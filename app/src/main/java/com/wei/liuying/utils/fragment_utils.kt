package com.wei.liuying.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.wei.liuying.R

fun showFragment(fragmentManager: FragmentManager, resId: Int, fragment: Fragment) {
    fragmentManager.beginTransaction()
        .setCustomAnimations(
            R.anim.right_in, R.anim.left_out,
            R.anim.left_in, R.anim.right_out
        ).replace(resId, fragment)
        .commit()
}

fun hideFragment(fragmentManager: FragmentManager, fragment: Fragment) {
    fragmentManager.popBackStack(fragment::class.simpleName, 0)
    fragmentManager.beginTransaction()
        .setCustomAnimations(
            R.anim.right_in, R.anim.left_out,
            R.anim.left_in, R.anim.right_out
        )
        .hide(fragment)
        .commit()
}

fun removeFragment(fragmentManager: FragmentManager, fragment: Fragment) {
    fragmentManager.popBackStack(fragment::class.simpleName, 0)
    fragmentManager.beginTransaction()
        .setCustomAnimations(
            R.anim.right_in, R.anim.left_out,
            R.anim.left_in, R.anim.right_out
        )
        .remove(fragment)
        .commit()
}