package com.cibertec.proyectogrupo4_dami.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.cibertec.proyectogrupo4_dami.R
import com.cibertec.proyectogrupo4_dami.data.AppDatabaseHelper

class RepartidorMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repartidor_menu)

        val idRepartidor = intent.getIntExtra("id_repartidor", 0)
        val tvPedidos = findViewById<TextView>(R.id.tvPedidosRepartidor)

        val dbHelper = AppDatabaseHelper(this)
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT id_pedido, fecha, estado, direccion, total FROM pedidos WHERE id_repartidor = ?",
            arrayOf(idRepartidor.toString())
        )

        val builder = StringBuilder()
        var direccionDelPedido: String? = null
        while (cursor.moveToNext()) {
            builder.append("Pedido #${cursor.getInt(0)}\n")
            builder.append("Fecha: ${cursor.getString(1)}\n")
            builder.append("Estado: ${cursor.getString(2)}\n")
            builder.append("Dirección: ${cursor.getString(3)}\n")
            builder.append("Total: S/ ${cursor.getDouble(4)}\n\n")
        }
        cursor.close()
        db.close()

        tvPedidos.text = if (builder.isEmpty()) "No tienes pedidos asignados" else builder.toString()

        val btnVerMapa = findViewById<Button>(R.id.btnVerMapa)
        btnVerMapa.setOnClickListener {
            val intent = Intent(this, RutaEntregaActivity::class.java)
            intent.putExtra("direccion", direccionDelPedido)
            startActivity(intent)
        }

    }
}