package com.vllenin.basemvvm.model.usecase.sample

import com.vllenin.basemvvm.base.extensions.convertResource
import com.vllenin.basemvvm.model.entities.Resource
import com.vllenin.basemvvm.model.entities.sample.RealItem
import com.vllenin.basemvvm.model.entities.sample.RealSampleData
import com.vllenin.basemvvm.model.repository.sample.ISampleRepository
import com.vllenin.basemvvm.model.usecase.BaseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * UseCase thuộc tầng Model(M): Trách nhiệm thể hiện business logic(logic xử lý data) của ứng dụng.
 * Khi lấy raw data từ Repository, nếu cần "xào nấu" lại dữ liệu, kết hợp dữ liệu từ nhiều
 * repository khác nhau để phục vụ presentation logic thì code thêm, còn không thì return luôn.
 *
 * UseCase sẽ không biết ViewModel(VM) là gì, tức là k giữ instance của VM. Chỉ tương tác lại VM
 * thông qua callback nếu cần.
 */
class SampleUseCase @Inject constructor(
    private val sampleRepository: ISampleRepository
) : BaseUseCase(), ISampleUseCase {

    /**
     * Ở đây dùng map để xử lý dữ liệu: Convert raw data nhận được
     * tử sampleRepository.getSample() là Resource<SampleData> -> Resource<RealSampleData>
     */
    override suspend fun getSample(): Flow<Resource<RealSampleData>> {
        return sampleRepository.getSample()
            .map { resource ->
                val listItem = ArrayList<RealItem>()
                resource.convertResource {
                    resource.data?.items?.forEach { item ->
                        listItem.add(RealItem("-*-${item.title}-*-"))
                    }

                    Resource.success(RealSampleData(resource.data?.title, listItem))
                }
            }
    }

}