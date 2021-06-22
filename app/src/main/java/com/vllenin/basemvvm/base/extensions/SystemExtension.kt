package com.vllenin.basemvvm.base.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.vllenin.basemvvm.R

/**
 * Created by Vllenin on 6/21/21.
 */
const val HEIGHT_STATUS_BAR_NORMAL_MIN = 24
const val HEIGHT_STATUS_BAR_NORMAL_MAX = 27

fun Context.getStatusBarHeight(): Int {
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    return when (resourceId > 0) {
        true -> {
            var height = resources.getDimensionPixelSize(resourceId)
            if (height <= 0) {
                height = resources.getDimensionPixelSize(R.dimen.height_status_bar_default)
            }
            height
        }
        else -> resources.getDimensionPixelSize(R.dimen.height_status_bar_default)
    }
}

fun Context.checkDeviceHasNotch(): Boolean {
    val density = resources.displayMetrics.density
    val height = getStatusBarHeight()
    val heightDp = (height / density).toInt()

    return heightDp !in HEIGHT_STATUS_BAR_NORMAL_MIN..HEIGHT_STATUS_BAR_NORMAL_MAX
}

fun Context.isNetworkConnection(): Boolean {
    var result = false
    val connectivityManager =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        result = when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } else {
        connectivityManager.run {
            connectivityManager.activeNetworkInfo?.run {
                result = when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }

            }
        }
    }

    return result
}