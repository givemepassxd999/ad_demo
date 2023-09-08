package com.example.ad_demo.network

import android.util.Log
import com.example.ad_demo.network.BaseUrl.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


object AppClientManager {
    //處理 Restful api log
    private var restfulLogging = HttpLoggingInterceptor { message ->
        Log.i(
            "restful interceptor_msg",
            message
        )
    }

    fun creteRestfulApiClient(): Retrofit {
        restfulLogging.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient().newBuilder().addInterceptor(restfulLogging).build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }
}