package com.giftox.app.network

import com.giftox.app.utils.Constants.BASE_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    @Volatile
    private var instance: Retrofit? = null
    private const val readTimeOut = 10L
    private const val connectAndWriteTimeOut = 100L

    fun getInstance(): Retrofit {
        return instance ?: synchronized(this) { create().also { instance = it } }
    }

    private fun create(): Retrofit {
        val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

        val client = OkHttpClient.Builder()
            .readTimeout(readTimeOut, TimeUnit.SECONDS)
            .connectTimeout(connectAndWriteTimeOut, TimeUnit.SECONDS)
            .writeTimeout(connectAndWriteTimeOut, TimeUnit.SECONDS)
            .addInterceptor(logger)
            .addInterceptor(HeaderInterceptors())
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    class HeaderInterceptors : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request().newBuilder()
                .addHeader("Accept", "application/json")
                .build()
            return chain.proceed(request)
        }

    }
}