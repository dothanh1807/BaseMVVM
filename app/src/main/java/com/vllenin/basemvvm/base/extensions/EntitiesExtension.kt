package com.vllenin.basemvvm.base.extensions

import android.content.SharedPreferences
import com.vllenin.basemvvm.model.entities.Resource
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Vllenin on 6/21/21.
 */
fun <X> Resource<*>.convertResource(callbackSuccess: () -> Resource<X>): Resource<X> {
    return when (status) {
        Resource.Status.LOADING -> {
            // IDE tự biết được type của Resource là <X>
            Resource.loading()
        }
        Resource.Status.SUCCESS -> {
            // Cuối body của callback sẽ phải return Resource.success(..)
            callbackSuccess.invoke()
        }
        Resource.Status.EXPIRED -> {
            Resource.expired(message ?: "EXPIRED")
        }
        else -> {
            Resource.error(message ?: "ERROR")
        }
    }
}

fun SharedPreferences.saveToSharedPreferences(key: String, value: Any, onCurrentThread: Boolean = false) {
    val shareEditor = edit()
    when (value) {
        is Int -> {
            shareEditor.putInt(key, value)
        }
        is Long -> {
            shareEditor.putLong(key, value)
        }
        is String -> {
            shareEditor.putString(key, value)
        }
        is Boolean -> {
            shareEditor.putBoolean(key, value)
        }
        is Float -> {
            shareEditor.putFloat(key, value)
        }
    }
    if (onCurrentThread) {
        shareEditor.commit()
    } else {
        shareEditor.apply()
    }
}

fun String.compareWithTime(currentTime: Long): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
    val mDate: Date? = sdf.parse(this)
    val timeInMilliseconds = mDate?.time ?: 0
    val timeComment = currentTime - timeInMilliseconds

    val minutes = timeComment / (1000 * 60)
    if (minutes < 1) {
        return "Vừa xong"
    } else if (minutes < 60) {
        return "$minutes phút trước"
    }

    val houses = minutes / 60
    if (houses < 24) {
        return "$houses giờ trước"
    }

    val days = houses / 24
    if (days < 30) {
        return "$days ngày trước"
    }

    val months = days / 30
    if (months < 12) {
        return "Khoảng $months tháng trước"
    }

    val years = months / 12
    return "Khoảng $years năm trước"
}