package com.vllenin.basemvvm.model.repository.sample

import com.vllenin.basemvvm.model.entities.Resource
import com.vllenin.basemvvm.model.entities.sample.SampleData
import kotlinx.coroutines.flow.Flow

/**
 * Created by Vllenin on 6/21/21.
 */
interface ISampleRepository {

    suspend fun getSample(): Flow<Resource<SampleData>>
    
}