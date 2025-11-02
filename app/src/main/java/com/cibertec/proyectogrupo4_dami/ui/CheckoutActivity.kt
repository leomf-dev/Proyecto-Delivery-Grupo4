package com.cibertec.proyectogrupo4_dami.ui

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cibertec.proyectogrupo4_dami.R
import com.cibertec.proyectogrupo4_dami.data.AppDatabaseHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CheckoutActivity : AppCompatActivity() {

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
        val idUsuario = intent.getIntExtra("id_usuario", 1) // usuario logueado

        val dbHelper = AppDatabaseHelper(this)
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT direccion, referencia FROM direccion WHERE id_usuario = ? LIMIT 1",
            arrayOf(idUsuario.toString())
        )

        if (cursor.moveToFirst()) {
            etDireccion.setText(cursor.getString(0))
            etReferencia.setText(cursor.getString(1))
        }
        cursor.close()

        tvTotal.text = "Total a pagar: S/ %.2f".format(total)

        btnConfirmar.setOnClickListener {
            val direccion = etDireccion.text.toString().trim()

            if (direccion.isEmpty()) {
                Toast.makeText(this, "Ingresa tu dirección", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val fecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
            val writableDb = dbHelper.writableDatabase

            val cursorRepartidor = writableDb.rawQuery(
                "SELECT id_repartidor FROM repartidor ORDER BY RANDOM() LIMIT 1",
                null
            )
            var idRepartidor: Int? = null
            if (cursorRepartidor.moveToFirst()) {
                idRepartidor = cursorRepartidor.getInt(0)
            }
            cursorRepartidor.close()

            val values = ContentValues().apply {
                put("id_usuario", idUsuario)
                put("id_repartidor", idRepartidor)
                put("fecha", fecha)
                put("total", total)
                put("estado", "En camino")
                put("direccion", direccion)
            }

            writableDb.insert("pedidos", null, values)
            writableDb.close()

            Toast.makeText(this, "Pedido realizado con éxito 🚴", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, EstadoPedidoActivity::class.java))
            finish()
        }
    }
}
