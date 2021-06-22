package com.vllenin.basemvvm.base.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.vllenin.basemvvm.R
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
fun FragmentManager.replaceScreen(screen: Fragment, tag: String) {
    beginTransaction()
        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
        .replace(R.id.screenContainer, screen, tag)
        .addToBackStack(null)
        .commit()
}

fun FragmentManager.addScreen(screen: Fragment, tag: String) {
    beginTransaction()
        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
        .add(R.id.screenContainer, screen, tag)
        .addToBackStack(null)
        .commit()
}