package com.cibertec.proyectogrupo4_dami.data

import com.cibertec.proyectogrupo4_dami.entity.Producto
import retrofit2.Call
import retrofit2.http.GET

interface ProductApiService {

    @GET("products.json")
    fun getProducts(): Call<Map<String, Producto>>
}

