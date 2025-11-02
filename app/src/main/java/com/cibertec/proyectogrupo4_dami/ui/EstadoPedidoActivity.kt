package com.cibertec.proyectogrupo4_dami.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cibertec.proyectogrupo4_dami.R
import com.cibertec.proyectogrupo4_dami.adapter.PedidoAdapter
import com.cibertec.proyectogrupo4_dami.data.AppDatabaseHelper
import com.cibertec.proyectogrupo4_dami.entity.Pedido

class EstadoPedidoActivity : AppCompatActivity() {
    private lateinit var btnSoporte: Button
    private lateinit var btnContacto: Button
    private lateinit var btnVerMiPedido: Button
    private lateinit var tvInfoRepartidor: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_estado_pedido)

        val rvPedidos = findViewById<RecyclerView>(R.id.rvPedidos)
        rvPedidos.layoutManager = LinearLayoutManager(this)
        btnSoporte = findViewById(R.id.btnSoporte)
        btnContacto = findViewById(R.id.btnContactarRepartidor)
        btnVerMiPedido = findViewById(R.id.btnVerMiPedido)
        tvInfoRepartidor = findViewById(R.id.tvInfoRepartidor)

        val dbHelper = AppDatabaseHelper(this)
        val db = dbHelper.readableDatabase
        val idUsuario = 1 // Simulado (login)

        // Obtener pedido más reciente con repartidor asignado
        val cursor = db.rawQuery(
            """
            SELECT p.id_pedido, p.fecha, p.estado, p.total, p.direccion,
                   r.nombre, r.celular
            FROM pedidos p
            LEFT JOIN repartidor r ON p.id_repartidor = r.id_repartidor
            WHERE p.id_usuario = ?
            ORDER BY p.id_pedido DESC LIMIT 1
            """.trimIndent(),
            arrayOf(idUsuario.toString())
        )

        var direccionCliente = ""
        if (cursor.moveToFirst()) {
            val nombreRep = cursor.getString(5)
            val celularRep = cursor.getString(6)
            direccionCliente = cursor.getString(4)
            val estado = cursor.getString(2)
            val total = cursor.getDouble(3)
            val fecha = cursor.getString(1)

            tvInfoRepartidor.text =
                "📦 Estado: $estado\n" +
                        "💰 Total: S/ $total\n" +
                        "📅 Fecha: $fecha\n\n" +
                        "🧍 Repartidor: $nombreRep\n" +
                        "📞 Celular: $celularRep"
        } else {
            tvInfoRepartidor.text = "No hay pedidos activos."
        }
        cursor.close()
        db.close()

        // Configurar botones
        btnSoporte.setOnClickListener {
            val url = "https://wa.me/51987654321?text=Hola%2C+necesito+ayuda+con+mi+pedido"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

        btnContacto.setOnClickListener {
            cambioAct(ContactoRepartidorActivity::class.java)
        }

        btnVerMiPedido.setOnClickListener {
            if (direccionCliente.isNotEmpty()) {
                val intent = Intent(this, RutaEntregaActivity::class.java)
                intent.putExtra("direccion", direccionCliente)
                startActivity(intent)
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ActivityEstadoPedido)) { v, insets ->
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                maxOf(systemBars.bottom, imeInsets.bottom)
            )
            insets
        }
    }

    fun cambioAct(activityDestino: Class<out Activity>) {
        val intent = Intent(this, activityDestino)
        startActivity(intent)
    }
}
