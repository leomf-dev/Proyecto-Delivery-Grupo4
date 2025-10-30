package com.cibertec.proyectogrupo4_dami.entity

import com.google.gson.annotations.SerializedName

data class Producto(
    //PARA QUE SE COMUNIQUE CON EL Gson
    val id: Int,
    @SerializedName("image") val imagen: String,
    @SerializedName("title") val titulo: String,
    @SerializedName("description") val descripcion: String,
    @SerializedName("price") val precio: Double,
    val cantidad: Int = 0
)