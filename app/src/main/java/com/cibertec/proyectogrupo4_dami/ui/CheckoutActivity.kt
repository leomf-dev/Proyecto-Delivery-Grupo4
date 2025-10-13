package com.cibertec.proyectogrupo4_dami.ui

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cibertec.proyectogrupo4_dami.data.AppDatabaseHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.cibertec.proyectogrupo4_dami.R

class CheckoutActivity: AppCompatActivity(){

    private lateinit var tvTotal: TextView
    private lateinit var etDireccion: EditText
    private lateinit var etReferencia: EditText
    private lateinit var btnConfirmar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        tvTotal = findViewById(R.id.tvTotal)
        etDireccion = findViewById(R.id.etDireccion)
        etReferencia = findViewById(R.id.etReferencia)
        btnConfirmar = findViewById(R.id.btnConfirmarPedido)

        val total = intent.getDoubleExtra("total", 0.0)
        val idUsuario = intent.getIntExtra("id_usuario", 1) // Simula usuario logueado

        tvTotal.text = "Total a pagar: S/ %.2f".format(total)

        btnConfirmar.setOnClickListener {
            val direccion = etDireccion.text.toString().trim()
            val referencia = etReferencia.text.toString().trim()

            if (direccion.isEmpty()) {
                Toast.makeText(this, "Ingresa tu dirección", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dbHelper = AppDatabaseHelper(this)
            val db = dbHelper.writableDatabase
            val fecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())

            val values = ContentValues().apply {
                put("id_usuario", idUsuario)
                put("fecha", fecha)
                put("total", total)
                put("estado", "Pendiente")
                put("direccion", direccion)
            }

            db.insert("pedidos", null, values)
            db.close()

            Toast.makeText(this, "Pedido realizado con éxito", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, EstadoPedidoActivity::class.java))
            finish()
        }
    }
}