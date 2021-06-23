package com.vllenin.basemvvm.model.usecase.sample

import com.vllenin.basemvvm.model.entities.Resource
import com.vllenin.basemvvm.model.entities.sample.RealSampleData
import kotlinx.coroutines.flow.Flow

/**
 * Created by Vllenin on 6/21/21.
 */
interface ISampleUseCase {

    suspend fun getSample(): Flow<Resource<RealSampleData>>

}