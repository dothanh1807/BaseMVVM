package com.vllenin.basemvvm.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vllenin.basemvvm.di.ViewModelFactory
import com.vllenin.basemvvm.ui.sample.SampleVM
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.vllenin.basemvvm.di.ViewModelKey

/**
 * Created by Vllenin on 3/26/21.
 */
@ExperimentalCoroutinesApi
@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SampleVM::class)
    internal abstract fun provideSampleViewModel(viewModel: SampleVM): ViewModel

}