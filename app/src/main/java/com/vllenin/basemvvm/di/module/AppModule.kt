package com.vllenin.basemvvm.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.vllenin.basemvvm.base.Constant
import com.vllenin.basemvvm.model.AppRetrofit
import com.vllenin.basemvvm.model.APISampleService
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

/**
 * Created by Vllenin on 4/5/21.
 */
@ExperimentalCoroutinesApi
@Module
class AppModule(private val application: Application) {

    @Provides
    @Singleton
    fun providesApplication(): Context = application

    @Provides
    @Singleton
    fun provideSharedPreference(context: Context): SharedPreferences {
        return context.getSharedPreferences(
            Constant.SHARE_PREFS_NAME,
            Context.MODE_PRIVATE
        )
    }

    @Provides
    @Singleton
    fun provideAPISampleService(): APISampleService {
        return AppRetrofit.getInstance().create(APISampleService::class.java)
    }

}