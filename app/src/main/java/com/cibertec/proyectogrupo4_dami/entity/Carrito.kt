package com.cibertec.proyectogrupo4_dami.entity

data class Carrito (
    val producto: Producto,
    var cantidad: Int
) {
    val subtotal = producto.precio * cantidad
   /* tvSubtotal.text = String.format("S/ %.2f", subtotal)    */
}

