package com.cibertec.proyectogrupo4_dami.entity

import com.google.gson.annotations.SerializedName

data class Producto(

    //PARA QUE SE COMUNIQUE CON EL Gson
    @SerializedName("id") val id: String,
    @SerializedName("imagen") val imagen: String,
    @SerializedName("titulo") val titulo: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("precio") val precio: Double,
    val cantidad: Int = 0
)