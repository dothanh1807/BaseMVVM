package com.vllenin.basemvvm.model

import com.vllenin.basemvvm.model.entities.HandlingResponse
import com.vllenin.basemvvm.model.entities.sample.SampleData
import retrofit2.Response
import retrofit2.http.GET

/**
 * Created by Vllenin on 6/18/21.
 */
interface APISampleService {

    @GET("string.txt")
    suspend fun requestSample(): Response<HandlingResponse<SampleData>>

}