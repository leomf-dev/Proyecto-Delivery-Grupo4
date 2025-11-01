package com.cibertec.proyectogrupo4_dami.entity

data class Carrito(

    val idProducto: String,
    val nombre: String,
    val precio: Double,
    val precioFinal: String,
    var cantidad: Int
)
{
    constructor() : this("", "", 0.0, "", 0)
}

