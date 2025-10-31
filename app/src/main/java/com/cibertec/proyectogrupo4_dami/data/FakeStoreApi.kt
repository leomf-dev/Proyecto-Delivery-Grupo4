package com.cibertec.proyectogrupo4_dami.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object FakeStoreApi {
    private const val BASE_URL = "https://deliveryaltoque-722b7-default-rtdb.firebaseio.com/"

    val apiService: ProductApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductApiService::class.java)
    }
}