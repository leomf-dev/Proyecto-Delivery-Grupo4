package com.cibertec.proyectogrupo4_dami.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.cibertec.proyectogrupo4_dami.R
import com.cibertec.proyectogrupo4_dami.entity.Pedido

class PedidoAdapter(private val pedidos: List<Pedido>) : RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder>(){

    inner class PedidoViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val ivProducto : ImageView = view.findViewById(R.id.ivProducto)
        val tvProducto : TextView = view.findViewById(R.id.tvProducto)
        val tvCantidad : TextView = view.findViewById(R.id.tvCantidad)
        val tvFecha : TextView = view.findViewById(R.id.tvFecha)
        val tvEstado : TextView = view.findViewById(R.id.tvEstado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pedido, parent, false)
        return PedidoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
        val pedido = pedidos[position]
        holder.tvProducto.text = pedido.nombreProducto
        holder.tvCantidad.text = "Cantidad: ${pedido.cantidad}"
        holder.tvFecha.text = "Fecha: ${pedido.fecha}"
        holder.tvEstado.text = "Estado: ${pedido.estado}"
        holder.ivProducto.setImageResource(pedido.imagen)

    }

    override fun getItemCount(): Int = pedidos.size
}