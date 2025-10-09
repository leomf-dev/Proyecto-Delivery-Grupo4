package com.cibertec.proyectogrupo4_dami.entity

data class Usuario(
    val nombres: String,
    val correo: String,
    val clave: String,
    val celular: String = ""
)