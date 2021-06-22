package com.vllenin.basemvvm.di.module

import com.vllenin.basemvvm.model.usecase.sample.ISampleUseCase
import com.vllenin.basemvvm.model.usecase.sample.SampleUseCase
import dagger.Binds
import dagger.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module
abstract class UseCaseModule {

    @Binds
    @Singleton
    abstract fun provideSampleUseCase(useCase: SampleUseCase): ISampleUseCase

}