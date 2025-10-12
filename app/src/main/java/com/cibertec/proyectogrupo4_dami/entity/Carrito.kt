package com.cibertec.proyectogrupo4_dami.entity

data class Carrito(
    val producto: Producto,
    var cantidad: Int
) {
    val subtotal: Double
        get() = cantidad * producto.precio.replace("S/ ", "").toDouble()



}
