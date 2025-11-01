package com.cibertec.proyectogrupo4_dami.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.cibertec.proyectogrupo4_dami.R
import com.cibertec.proyectogrupo4_dami.entity.Pedido
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PedidoAdapter(
    private val context: Context,
    private val listaPedidos: MutableList<Pedido>
) : RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_pedido, parent, false)
        return PedidoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
        val pedido = listaPedidos[position]

        holder.tvNombreProducto.text = pedido.nombreProducto
        holder.tvCantidad.text = "Cantidad: ${pedido.cantidad}"
        holder.tvFecha.text = "Fecha: ${pedido.fecha}"
        holder.tvEstado.text = "Estado: ${pedido.estado}"

        // Colorear estado visualmente
        when (pedido.estado.lowercase()) {
            "entregado" -> holder.tvEstado.setTextColor(context.getColor(R.color.verde))
            "pendiente" -> holder.tvEstado.setTextColor(context.getColor(R.color.naranja))
            "cancelado" -> holder.tvEstado.setTextColor(context.getColor(R.color.rojo))
        }
    }

    override fun getItemCount(): Int = listaPedidos.size

    inner class PedidoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombreProducto: TextView = itemView.findViewById(R.id.tvProducto)
        val tvCantidad: TextView = itemView.findViewById(R.id.tvCantidad)
        val tvFecha: TextView = itemView.findViewById(R.id.tvFecha)
        val tvEstado: TextView = itemView.findViewById(R.id.tvEstado)
    }

    // Metodo para cargar pedidos desde Firebase
    fun cargarPedidosDesdeFirebase() {
        val firebaseAuth = FirebaseAuth.getInstance()
        val uid = firebaseAuth.uid ?: return

        val ref = FirebaseDatabase.getInstance().getReference("usuarios").child(uid).child("Pedidos")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaPedidos.clear()
                if (snapshot.exists()) {
                    for (pedidoSnap in snapshot.children) {
                        val pedido = pedidoSnap.getValue(Pedido::class.java)
                        if (pedido != null) {
                            listaPedidos.add(pedido)
                        }
                    }
                    notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error al cargar pedidos: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}