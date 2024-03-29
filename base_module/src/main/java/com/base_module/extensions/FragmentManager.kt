package com.base_module.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.base_module.R


fun FragmentManager.addFragmentWithFadeIn(containerViewId: Int, fragment: Fragment, tag: String?) {
    this.beginTransaction()
        .addToBackStack(tag)
        .setCustomAnimations(R.anim.anim_fade_in,R.anim.anim_fade_in, R.anim.anim_fade_out, R.anim.anim_fade_out)
        .add(containerViewId, fragment, tag)
        .commitAllowingStateLoss()
}

fun FragmentManager.addFragmentFromBottom(containerViewId: Int,fragment: Fragment,tag: String) {
    this.beginTransaction()
        .addToBackStack(tag)
        .setCustomAnimations(R.anim.bottom_in,R.anim.bottom_out,R.anim.bottom_in,R.anim.bottom_out)
        .add(containerViewId,fragment,tag)
        .commitAllowingStateLoss()
}

fun FragmentManager.replaceFragmentFromBottom(containerViewId: Int,fragment: Fragment,tag: String) {
    this.beginTransaction()
        .setCustomAnimations(R.anim.bottom_in,R.anim.bottom_out)
        .replace(containerViewId,fragment,tag)
        .commitAllowingStateLoss()
}

fun FragmentManager.removeFragmentFromBottom(fragment: Fragment) {
    this.beginTransaction()
        .setCustomAnimations(R.anim.bottom_in, R.anim.bottom_out)
        .remove(fragment)
        .commitAllowingStateLoss()

}