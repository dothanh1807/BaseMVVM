package com.vllenin.basemvvm.model.repository

import com.vllenin.basemvvm.model.entities.HandlingResponse
import com.vllenin.basemvvm.model.entities.Resource
import retrofit2.Response

open class BaseRepository {

    /**
     * Do API nào cũng cần gọi function này, nên cho code vào BaseRepository.
     * Nếu function nào có lúc dùng, lúc không thì nên cho vào Extension, chứ k nên cho vào open class
     * để kế thừa.
     */
    protected suspend fun <T> getResponse(request: suspend () -> Response<HandlingResponse<T>>): Resource<T> {
        return try {
            val result = request.invoke()
            if (result.isSuccessful) {
                return when ((result.body() as HandlingResponse<*>).status.code) {
                    200 -> Resource.success((result.body() as HandlingResponse<*>).data as T)
                    401 -> Resource.expired((result.body() as HandlingResponse<*>).status.message ?: "")
                    else -> Resource.error((result.body() as HandlingResponse<*>).status.message ?: "")
                }
            } else {
                Resource.error(result.message())
            }
        } catch (e: Throwable) {
//            FirebaseCrashlytics.getInstance().recordException(Exception("CustomException: From getResponse ${e.message}"))
            Resource.error("ERROR")
        }
    }

}