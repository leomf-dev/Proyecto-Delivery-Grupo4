package com.cibertec.proyectogrupo4_dami.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cibertec.proyectogrupo4_dami.R
import com.cibertec.proyectogrupo4_dami.adapter.PedidoAdapter
import com.cibertec.proyectogrupo4_dami.entity.Pedido

class EstadoPedidoActivity : AppCompatActivity() {
    private lateinit var btnSoporte : Button
    private lateinit var btnContacto : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_estado_pedido)

//        val rvPedidos = findViewById<RecyclerView>(R.id.rvPedidos)
//        rvPedidos.layoutManager = LinearLayoutManager(this)
//
//        val listaPedidos = listOf(
//            Pedido(
//                "Hamburguesa Doble",
//                2,
//                "07/10/2025",
//                "En camino",
//                R.drawable.hamburgesa_img_referencial
//            )
//        )
//
//        rvPedidos.adapter = PedidoAdapter(listaPedidos)

        btnSoporte = findViewById(R.id.btnSoporte)
        btnContacto = findViewById(R.id.btnContactarRepartidor)

        btnSoporte.setOnClickListener {
            val url = "https://wa.me/51987654321?text=Hola%2C+necesito+ayuda+con+mi+pedido"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        btnContacto.setOnClickListener {
            cambioAct(ContactoRepartidorActivity::class.java)
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

    fun cambioAct(activityDestino : Class<out Activity>){
        val intent = Intent(this, activityDestino)
        startActivity(intent)
    }
}