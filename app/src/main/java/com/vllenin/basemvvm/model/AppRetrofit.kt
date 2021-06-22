package com.vllenin.basemvvm.model

import android.os.Build
import com.vllenin.basemvvm.BuildConfig
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Vllenin on 6/23/20.
 */
@ExperimentalCoroutinesApi
class AppRetrofit {

    companion object {
        @JvmStatic
        private var INSTANCE: Retrofit? = null
        const val TOKEN = ""
        const val BASE_URL = "https://tugiavandat.com/"

        fun getInstance() =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildRetrofit().also { retrofit ->
                    INSTANCE = retrofit
                }
            }

        private fun buildRetrofit(): Retrofit {
            val client = OkHttpClient.Builder()
            client.connectTimeout(30, TimeUnit.SECONDS)
            client.readTimeout(30, TimeUnit.SECONDS)
            client.addInterceptor { chain ->
                var request = chain.request()
                val url = request.url().newBuilder()
                request = request.newBuilder()
                    .addHeader("Authorization", "Bearer $TOKEN")
                    .addHeader("platform", "Android-${Build.MODEL}-${Build.VERSION.SDK_INT}-${BuildConfig.VERSION_NAME}")
                    .url(url.build())
                    .build()
                chain.proceed(request)
            }

            return Retrofit.Builder()
              .baseUrl(BASE_URL)
              .addConverterFactory(GsonConverterFactory.create())
              .client(client.build())
              .build()
        }

    }

}