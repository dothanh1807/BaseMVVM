package com.vllenin.basemvvm.base.common

import android.os.SystemClock
import android.view.View
import com.vllenin.basemvvm.base.ApplicationX
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Created by Nhibeohonheo_kut3 on 8/7/20.
 */
@ExperimentalCoroutinesApi
abstract class OnSingleClickListener(
    private val timeInterval: Int
) : View.OnClickListener {

    companion object {
        const val MIN_CLICK_INTERVAL_SHORT = 300
        const val MIN_CLICK_INTERVAL_NORMAL = 500
        const val MIN_CLICK_INTERVAL_LONG = 1000
        const val MIN_CLICK_INTERVAL_XLONG = 1500
    }

    abstract fun onSingleClick(view: View?)

    override fun onClick(v: View?) {
        val currentClickTime = SystemClock.uptimeMillis()
        val elapsedTime = currentClickTime - ApplicationX.lastClickTime

        if (elapsedTime <= timeInterval) {
            return
        }

        onSingleClick(v)
        ApplicationX.lastClickTime = currentClickTime
    }

}