package com.cibertec.proyectogrupo4_dami.entity

import java.io.Serializable

data class Pedido(
    val idPedido: String = "",
    val idUsuario: String = "",
    val listaProductos: List<ProductoPedido> = emptyList(),
    val total: Double = 0.0,
    val fecha: Long = 0L,
    val estado: String = "Pendiente"
) : Serializable

data class ProductoPedido(
    val nombreProducto: String = "",
    val cantidad: Int = 0,
    val precioFinal: Double = 0.0
) : Serializable