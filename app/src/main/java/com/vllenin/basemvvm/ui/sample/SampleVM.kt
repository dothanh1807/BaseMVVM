package com.vllenin.basemvvm.ui.sample

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vllenin.basemvvm.base.BaseVM
import com.vllenin.basemvvm.model.entities.Resource
import com.vllenin.basemvvm.model.entities.sample.Item
import com.vllenin.basemvvm.model.entities.sample.SampleData
import com.vllenin.basemvvm.model.usecase.sample.ISampleUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Tầng ViewModel(VM) chỉ nên có presentation logic (logic hiển thị chế độ xem), KHÔNG nên có
 * business logic (logic xử lý dữ liệu)
 *
 * ViewModel(VM) sẽ không biết View(V) là gì, tức là k giữ instance của View(V). Chỉ tương tác
 * lại View(V) thông qua callback.
 */
class SampleVM @Inject constructor(
    private val sampleUseCase: ISampleUseCase
) : BaseVM() {

    /**
     * _sampleData là MutableLiveData -> Có thể đọc/ghi -> Cần để private,
     * tránh việc View có thể thay đổi TRỰC TIẾP dữ liệu của ViewModel.
     * sampleData là LiveData -> Chỉ đọc -> Để public cho View dùng(observe)
     */
    private val _sampleData = MutableLiveData<Resource<SampleData>>()
    val sampleData: LiveData<Resource<SampleData>> = _sampleData

    fun fetchSampleContent() {
        viewModelScope.launch {
            sampleUseCase.getSample()
                .collect { resource ->
                    launch(Dispatchers.Main.immediate) {
                        _sampleData.value = resource.getResource()
                    }
                }
        }
    }

    /**
     * ViewModel(VM) tương tác lại View(V) thông qua callback, chứ VM k nên giữ instance của View(V)
     */
    fun requestDataOf(item: Item?, callback: (data: String) -> Unit) {
        val fakeData = "${item?.title.hashCode()}"

        callback.invoke(fakeData)
    }

}