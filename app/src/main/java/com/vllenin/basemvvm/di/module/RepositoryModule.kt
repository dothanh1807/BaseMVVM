package com.vllenin.basemvvm.di.module

import com.vllenin.basemvvm.model.repository.sample.ISampleRepository
import com.vllenin.basemvvm.model.repository.sample.SampleRepository
import dagger.Binds
import dagger.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

/**
 * Created by Vllenin on 3/26/21.
 */
@ExperimentalCoroutinesApi
@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideUserRepository(repository: SampleRepository): ISampleRepository

}