package com.vllenin.basemvvm.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by Vllenin on 3/26/21.
 */
class ViewModelFactory @Inject constructor(private val viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return viewModels[modelClass]?.get() as T

//        var creator: Provider<out ViewModel>? = viewModels[modelClass]
//        if (creator == null) {
//            for ((key, value) in viewModels) {
//                if (modelClass.isAssignableFrom(key)) {
//                    creator = value
//                    break
//                }
//            }
//        }
//        if (creator == null) {
//            throw IllegalArgumentException("Unknown model class $modelClass")
//        }
//        try {
//            return creator.get() as T
//        } catch (e: Exception) {
//            throw RuntimeException(e)
//        }
    }

}