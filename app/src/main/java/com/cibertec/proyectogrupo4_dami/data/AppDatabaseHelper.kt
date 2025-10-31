package com.cibertec.proyectogrupo4_dami.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


/**
Clase para gestionar la creación y actualización de la base de datos SQLite.
 */

class AppDatabaseHelper (context: Context) : SQLiteOpenHelper(context, "altoque_app.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {


        // Tabla USUARIO
        db.execSQL("""
            CREATE TABLE usuario (
                id_usuario INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                nombres TEXT NOT NULL,
                correo TEXT NOT NULL UNIQUE,
                clave TEXT NOT NULL,
                celular TEXT NOT NULL
            )
        """.trimIndent()) //Elimina espacios y saltos innecesarios


        // Tabla CATEGORIA
        db.execSQL("""
            CREATE TABLE categoria (
                id_categoria INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                nom_categ TEXT NOT NULL
            )
        """.trimIndent())


        // Tabla PRODUCTO
        db.execSQL("""
            CREATE TABLE producto (
                id_producto INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                id_categoria INTEGER NOT NULL,
                imagen TEXT,
                nom_producto TEXT NOT NULL,
                descripcion TEXT,
                precio TEXT NOT NULL,
                cantidad INTEGER NOT NULL,
                FOREIGN KEY(id_categoria) REFERENCES categoria(id_categoria)
            )
         """.trimIndent())


        // Tabla CARRITO
        db.execSQL("""
            CREATE TABLE carrito (
                id_carrito INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                id_producto INTEGER NOT NULL,
                cantidad INTEGER NOT NULL,
                subtotal TEXT NOT NULL,
                FOREIGN KEY(id_producto) REFERENCES producto(id_producto)
            )
        """.trimIndent())



        // Tabla PEDIDOS
        db.execSQL("""
    CREATE TABLE pedidos (
        id_pedido INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
        id_usuario INTEGER NOT NULL,
        fecha TEXT NOT NULL,
        total REAL NOT NULL,
        estado TEXT NOT NULL,
        direccion TEXT NOT NULL,
        FOREIGN KEY(id_usuario) REFERENCES usuario(id_usuario)
    )
""".trimIndent())



        // Tabla DIRECCION
        db.execSQL("""
    CREATE TABLE direccion (
        id_direccion INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
        id_usuario INTEGER NOT NULL,
        direccion TEXT NOT NULL,
        referencia TEXT,
        latitud REAL,
        longitud REAL,
        FOREIGN KEY(id_usuario) REFERENCES usuario(id_usuario)
    )
""".trimIndent())

    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Elimina todas las tablas si se actualiza la versión
        db.execSQL("DROP TABLE IF EXISTS direccion")
        db.execSQL("DROP TABLE IF EXISTS pedidos")
        db.execSQL("DROP TABLE IF EXISTS carrito")
        db.execSQL("DROP TABLE IF EXISTS producto")
        db.execSQL("DROP TABLE IF EXISTS categoria")
        db.execSQL("DROP TABLE IF EXISTS usuario")

        // Vuelve a crearlas
        onCreate(db)

    }
}