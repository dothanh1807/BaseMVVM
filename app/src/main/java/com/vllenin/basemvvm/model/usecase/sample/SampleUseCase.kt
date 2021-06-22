package com.vllenin.basemvvm.model.usecase.sample

import com.vllenin.basemvvm.model.entities.Resource
import com.vllenin.basemvvm.model.entities.sample.SampleData
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
     * Ở đây dùng map để xử lý dữ liệu
     */
    override suspend fun getSample(): Flow<Resource<SampleData>> {
        return sampleRepository.getSample()
            .map { resource ->
                resource.data?.items?.forEach { item ->
                    item.title = "-*-${item.title}-*-"
                }

                resource
            }
    }

}