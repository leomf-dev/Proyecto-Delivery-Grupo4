package com.cibertec.proyectogrupo4_dami.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cibertec.proyectogrupo4_dami.R
import com.cibertec.proyectogrupo4_dami.entity.Producto

class ProductApiAdapter(private val listaProductos: List<Producto>) :
    RecyclerView.Adapter<ProductApiAdapter.ProductApiViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductApiViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_categoria, parent, false)
        return ProductApiViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductApiViewHolder, position: Int) {
        val producto = listaProductos[position]

        // Cargar imagen desde URL con Glide
        Glide.with(holder.itemView.context)
            .load(producto.imagen)

            //.placeholder(R.drawable.ic_food) // opcional: imagen por defecto
            //.error(R.drawable.ic_food)       // opcional: si falla

            .into(holder.ivImagen)

        holder.tvProducto.text = producto.titulo
        holder.tvPrecio.text = "S/ ${producto.precio}"
        holder.tvDescrip.text = producto.descripcion

        // Opcional: ocultar el botón si no lo usas en la API
        // holder.btnComprar.visibility = View.GONE
    }

    override fun getItemCount(): Int = listaProductos.size

    inner class ProductApiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivImagen: ImageView = itemView.findViewById(R.id.ivProducto)
        val tvProducto: TextView = itemView.findViewById(R.id.Tv_Producto)
        val tvPrecio: TextView = itemView.findViewById(R.id.Tv_Precio)
        val tvDescrip: TextView = itemView.findViewById(R.id.Tv_Descripcion)

        // Si no usas el botón, puedes omitirlo, pero no rompe dejarlo
        // val btnComprar = itemView.findViewById<MaterialButton>(R.id.btnComprar)
    }
}