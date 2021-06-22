package com.vllenin.basemvvm.base

import androidx.lifecycle.ViewModel
import com.vllenin.basemvvm.model.entities.Resource

/**
 * Created by Vllenin on 6/21/21.
 */
open class BaseVM : ViewModel() {

    protected fun <T> Resource<T>.getResource(callbackSuccess: (data: T) -> Unit = {}) : Resource<T> {
        return when (status) {
            Resource.Status.LOADING -> {
                Resource.loading()
            }
            Resource.Status.SUCCESS -> {
                val data = data as T
                callbackSuccess.invoke(data)
                Resource.success(data)
            }
            Resource.Status.EXPIRED -> {
                Resource.expired(message ?: "")
            }
            else -> {
                Resource.error(message ?: "")
            }
        }
    }

}