package com.vllenin.basemvvm.base

import android.app.Application
import android.util.Size
import com.vllenin.basemvvm.di.component.ApplicationComponent
import com.vllenin.basemvvm.di.component.DaggerApplicationComponent
import com.vllenin.basemvvm.di.module.AppModule
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Created by Vllenin on 8/28/20.
 */
@ExperimentalCoroutinesApi
class ApplicationX : Application() {

    lateinit var appComponent: ApplicationComponent

    companion object {
        var lastClickTime = 0L
        var isNetworkAvailable = true
        var SCREEN_SIZE = Size(0, 0)
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

    override fun onLowMemory() {
        super.onLowMemory()
//        Glide.get(this).clearMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
//        Glide.get(this).trimMemory(level)
    }

}