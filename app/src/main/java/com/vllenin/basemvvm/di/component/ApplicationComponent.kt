package com.vllenin.basemvvm.di.component

import com.vllenin.basemvvm.ui.MainActivity
import com.vllenin.basemvvm.di.module.*
import com.vllenin.basemvvm.ui.sample.SampleScreen
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

/**
 * Created by Vllenin on 3/26/21.
 */
@ExperimentalCoroutinesApi
@Singleton
@Component(modules = [
    AppModule::class,
    RepositoryModule::class,
    UseCaseModule::class,
    ViewModelModule::class
])
interface ApplicationComponent {

    fun inject(activity: MainActivity)

    fun inject(fragment: SampleScreen)

}