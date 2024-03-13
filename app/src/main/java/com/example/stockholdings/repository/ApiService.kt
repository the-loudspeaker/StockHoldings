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
