package com.cibertec.proyectogrupo4_dami.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cibertec.proyectogrupo4_dami.R
import com.cibertec.proyectogrupo4_dami.entity.Producto
import com.cibertec.proyectogrupo4_dami.ui.CarritoActivity
import com.google.android.material.button.MaterialButton

class HistorialAdapter(private val listaHistorial: List<Producto>) :
    RecyclerView.Adapter<HistorialAdapter.HistorialViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistorialViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_categoria, parent, false)
        return HistorialViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistorialViewHolder, position: Int) {
        val product = listaHistorial[position]

        Glide.with(holder.itemView.context)
            .load(product.imagen)
            .into(holder.ivImagen)

        holder.tvProducto.text = product.titulo
        holder.tvPrecio.text = "S/ ${product.precio}"
        holder.tvDescrip.text = product.descripcion
        //------------------------------------------------

        //  Cuando el usuario presione el botón "Comprar"
        holder.btnComprar.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, CarritoActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listaHistorial.size
    }

    inner class HistorialViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivImagen: ImageView = itemView.findViewById(R.id.ivProducto)
        val tvProducto: TextView = itemView.findViewById(R.id.Tv_Producto)
        val tvPrecio: TextView = itemView.findViewById(R.id.Tv_Precio)
        val tvDescrip: TextView = itemView.findViewById(R.id.Tv_Descripcion)
        val btnComprar: MaterialButton = itemView.findViewById(R.id.btnComprar)
    }
}
