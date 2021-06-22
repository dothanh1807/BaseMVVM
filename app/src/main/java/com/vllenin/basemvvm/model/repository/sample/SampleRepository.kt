package com.vllenin.basemvvm.model.repository.sample

import com.vllenin.basemvvm.model.APISampleService
import com.vllenin.basemvvm.model.entities.Resource
import com.vllenin.basemvvm.model.entities.sample.SampleData
import com.vllenin.basemvvm.model.repository.BaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Repository thuộc tầng Model(M): Trách nhiệm lấy raw data(dữ liệu nguyên bản) từ Server hoặc DataBase Local
 *
 * Repository k biết UseCase là gì, tức là k giữ instance của UseCase. Chỉ tương tác lại UseCase
 * thông qua callback nếu cần.
 */
class SampleRepository @Inject constructor(
    private val apiSampleService: APISampleService
) : BaseRepository(), ISampleRepository {

    override suspend fun getSample(): Flow<Resource<SampleData>> {
        return flow {
            emit(Resource.loading())
            val resource = getResponse { apiSampleService.requestSample() }
            emit(resource)
        }
    }

}