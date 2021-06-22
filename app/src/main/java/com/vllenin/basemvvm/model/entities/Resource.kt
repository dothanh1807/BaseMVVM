package com.vllenin.basemvvm.model.entities

/**
 * Created by Vllenin on 3/26/21.
 */
data class Resource<T>(val status: Status, val data: T?, val message: String?) {

    enum class Status {
        SUCCESS,
        EXPIRED,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> expired(message: String): Resource<T> {
            return Resource(Status.EXPIRED, null, message)
        }

        fun <T> error(message: String, data: T? = null): Resource<T> {
            return Resource(Status.ERROR, data, message)
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }
    }
}