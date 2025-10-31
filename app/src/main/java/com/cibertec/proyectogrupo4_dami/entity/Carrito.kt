package com.cibertec.proyectogrupo4_dami.entity

data class Carrito(
    val producto: Producto,
    var cantidad: Int
) {
    //CAMBIADO PARA DOUBLE
    val subtotal: Double
        get() = cantidad * producto.precio
}
