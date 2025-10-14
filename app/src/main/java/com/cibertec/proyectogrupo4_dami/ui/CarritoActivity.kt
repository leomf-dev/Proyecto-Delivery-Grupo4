package com.cibertec.proyectogrupo4_dami.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cibertec.proyectogrupo4_dami.Fragment.Inicio_MenuActivity
import com.cibertec.proyectogrupo4_dami.R
import com.cibertec.proyectogrupo4_dami.adapter.CarritoAdapter
import com.cibertec.proyectogrupo4_dami.entity.Carrito
import com.cibertec.proyectogrupo4_dami.entity.Producto
import com.google.android.material.button.MaterialButton

class CarritoActivity : AppCompatActivity() {

    private lateinit var rvCarrito: RecyclerView
    private lateinit var tvTotalGeneral: TextView
    private lateinit var btnRealizarPedido: MaterialButton
    private lateinit var btnContinuarComprando: MaterialButton
    private lateinit var carritoAdapter: CarritoAdapter
    private val listaCarrito = mutableListOf<Carrito>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_carrito)

        rvCarrito = findViewById(R.id.rvCarrito)
        tvTotalGeneral = findViewById(R.id.tvTotalGeneral)
        btnContinuarComprando = findViewById(R.id.btnContinuarComprando)
        btnRealizarPedido = findViewById(R.id.btnRealizarPedido)


        // Datos de prueba (puedes reemplazar por los que vengan del intent)
        listaCarrito.addAll(
            listOf(
                Carrito(Producto(R.mipmap.ic_hamburguesa1, "Hamburguesa Clásica", "Crujiente", "S/ 13.00", 1), 1),
                Carrito(Producto(R.mipmap.ic_hamburguesa2, "Hamburguesa Queso", "Jugosa", "S/ 18.00", 1), 2)
            )
        )

        carritoAdapter = CarritoAdapter(listaCarrito) { total ->
            tvTotalGeneral.text = "Total: S/ %.2f".format(total)
        }

        rvCarrito.layoutManager = LinearLayoutManager(this)
        rvCarrito.adapter = carritoAdapter

        //-----------------------------------------------------CAMBIAR ACITVITY----------------
        btnContinuarComprando.setOnClickListener {
            val intent = Intent(this, Inicio_MenuActivity::class.java)
            startActivity(intent)
            finish() // Cierra el carrito para volver a la categoría
        }

        btnRealizarPedido.setOnClickListener {
            // Aquí podrías mostrar un mensaje o navegar a otra pantalla
        }
    }
}