package com.cibertec.proyectogrupo4_dami.entity

data class Usuario(
    val id: Int,
    val nombres: String,
    val apellidos: String,
    val correo: String,
    val clave: String,
    val celular: String = "",
    val dni: String = ""
)