package com.cibertec.proyectogrupo4_dami.entity

data class Pedido(
    val nombreProducto: String,
    val cantidad: Int,
    val fecha: String,
    val estado: String,
    val imagen: Int
)