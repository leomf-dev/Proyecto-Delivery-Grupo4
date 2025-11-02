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
import com.cibertec.proyectogrupo4_dami.ui.DetallePedidoDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PedidoAdapter(
    private val context: Context,
    private val listaPedidos: MutableList<Pedido>,
    private val onPedidoClick: (Pedido) -> Unit
) : RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_pedido, parent, false)
        return PedidoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
        val pedido = listaPedidos[position]

        val fechaFormateada = convertirTimestampAFecha(pedido?.fecha)

        holder.tvNombreProducto.text = "Código: #${pedido.idPedido.takeLast(6)}"
        holder.tvFecha.text = "Fecha: $fechaFormateada"
        holder.tvTotal.text = "Total: ${pedido.total}"
        holder.tvEstado.text = "Estado: ${pedido.estado}"

        // Colorear estado visualmente
        when (pedido.estado.lowercase()) {
            "entregado" -> holder.tvEstado.setTextColor(context.getColor(R.color.verde))
            "pendiente" -> holder.tvEstado.setTextColor(context.getColor(R.color.naranja))
            "cancelado" -> holder.tvEstado.setTextColor(context.getColor(R.color.rojo))
        }

        holder.itemView.setOnClickListener { onPedidoClick(pedido) }
    }

    override fun getItemCount(): Int = listaPedidos.size

    inner class PedidoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombreProducto: TextView = itemView.findViewById(R.id.tvProducto)
        val tvTotal: TextView = itemView.findViewById(R.id.tvTotal)
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
                        if (pedido != null) listaPedidos.add(pedido)
                    }
                    notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error al cargar pedidos: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun convertirTimestampAFecha(timestamp: Long?): String {
        // Si el timestamp es nulo o cero, devuelve un texto por defecto
        if (timestamp == null || timestamp == 0L) {
            return "Fecha no disponible"
        }
        // El formato "dd/MM/yyyy" mostrará día/mes/año. Puedes cambiarlo.
        val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val fechaDate = Date(timestamp)
        return formato.format(fechaDate)
    }
}