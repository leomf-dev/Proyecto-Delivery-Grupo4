package com.cibertec.proyectogrupo4_dami

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Categoria_1 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_categoria1)

        //----------------- LISTA DE CATEGORÍAS -----------------
        val categorias = listOf(
            Pair(R.id.layoutCategoria1, "Hamburguesas Clásicas 🍔"),
            Pair(R.id.layoutCategoria2, "Hamburguesas Especiales 🌶️"),
            Pair(R.id.layoutCategoria3, "Bebidas 🥤"),
            Pair(R.id.layoutCategoria4, "Postres 🍰"),
            Pair(R.id.layoutCategoria5, "Pizzas 🍕"),
            Pair(R.id.layoutCategoria6, "Pollos 🍗"),
            Pair(R.id.layoutCategoria7, "Tacos 🌮"),
            Pair(R.id.layoutCategoria8, "Sandwiches 🥪"),
            Pair(R.id.layoutCategoria9, "Combos 🍟"),
            Pair(R.id.layoutCategoria10, "Snacks 🍿"),
            Pair(R.id.layoutCategoria1_0, "Jugos Naturales 🍊"),
            Pair(R.id.layoutCategoria2_0, "Ofertas 💥"),
            Pair(R.id.layoutCategoria3_0, "Ensaladas 🥗"),
            Pair(R.id.layoutCategoria4_0, "Menús del Día 🍽️"),
            Pair(R.id.layoutCategoria5_0, "Cafés ☕")
        )

        //-------------------------------------------------------
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //------------------ APLICAR EFECTO + POPUP -----------------------
        categorias.forEach { (id, nombre) ->
            val layout = findViewById<LinearLayout>(id)
            aplicarEfectoZoom(layout)
            layout.setOnClickListener {
                mostrarPopupPersonalizado(nombre)
            }
        }
    }

    // -------------Función de zoom reutilizable--------------
    private fun aplicarEfectoZoom(vista: View) {
        vista.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.animate().scaleX(1.1f).scaleY(1.1f).setDuration(150).start()
                    v.elevation = 10f
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    v.animate().scaleX(1f).scaleY(1f).setDuration(150).start()
                    v.elevation = 0f
                }
            }
            false // 👈 importante: permite que también se detecte el clic
        }
    }

    //-----------MOSTRAR VENTANA EMERGENTE-----------------
    private fun mostrarPopupPersonalizado(nombreCategoria: String) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.popup_categoria)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val txtTitulo = dialog.findViewById<TextView>(R.id.txtTitulo)
        txtTitulo.text = nombreCategoria

        val btnCerrar = dialog.findViewById<Button>(R.id.btnCerrar)
        btnCerrar.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }
}