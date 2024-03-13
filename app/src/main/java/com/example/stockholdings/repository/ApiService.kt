package com.example.stockholdings.repository

import com.example.stockholdings.model.StockResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface ApiService {
    @GET("/v3/bde7230e-bc91-43bc-901d-c79d008bddc8")
    suspend fun getData(): StockResponse
}

class DataRepository {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://run.mocky.io") // Replace with your actual base URL
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    private val apiService = retrofit.create(ApiService::class.java)

    suspend fun getData(): StockResponse {
        return apiService.getData()
    }
}