package com.cibertec.proyectogrupo4_dami.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cibertec.proyectogrupo4_dami.R
import com.cibertec.proyectogrupo4_dami.adapter.PedidoAdapter
import com.cibertec.proyectogrupo4_dami.entity.Pedido
import com.cibertec.proyectogrupo4_dami.ui.ContactoRepartidorActivity

class PedidosFragment : Fragment(R.layout.fragment_pedido) {

    private lateinit var rvPedidos : RecyclerView
    private lateinit var btnContactarRepartidor : Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvPedidos = view.findViewById(R.id.rvPedidos)
        btnContactarRepartidor = view.findViewById(R.id.btnContactarRepartidor)

        btnContactarRepartidor.setOnClickListener {
            startActivity(Intent(requireContext(), ContactoRepartidorActivity::class.java))
        }

        rvPedidos.layoutManager = LinearLayoutManager(requireContext())

        val listaPedidos = mutableListOf<Pedido>()

        val adapter = PedidoAdapter(requireContext(), listaPedidos)
        rvPedidos.adapter = adapter

        adapter.cargarPedidosDesdeFirebase()
    }
}