package com.cibertec.proyectogrupo4_dami.adapter

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cibertec.proyectogrupo4_dami.R
import com.cibertec.proyectogrupo4_dami.entity.Producto
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView


class ProductApiAdapter(
    private val context: Context,
    private val listaProductos: List<Producto>) :
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
            .transform(CircleCrop())

            //.placeholder(R.drawable.ic_food) // opcional: imagen por defecto
            //.error(R.drawable.ic_food)       // opcional: si falla

            .into(holder.ivImagen)

        holder.tvProducto.text = producto.titulo
        holder.tvPrecio.text = "S/ ${producto.precio}"
        holder.tvDescrip.text = producto.descripcion

        // Opcional: ocultar el botón si no lo usas en la API

        //Para agregar al carrito el producto seleccionado
        holder.btnComprar.setOnClickListener {
            verCarrito(producto)
        }
    }


    private fun verCarrito(producto: Producto){
        //vistas
        var imagenSIV : ShapeableImageView
        var nombretv : TextView
        var descripciontv : TextView
        var preciotv : TextView
        var prefinaltv : TextView
        var btnDisminuir : ImageButton
        var cantidadtv : TextView
        var btnAumentar : ImageButton
        var btnAgregarCarrito : MaterialButton

        val dialog = Dialog(context)
        dialog.setContentView(R.layout.carrito_compras) // vista de carrito

        imagenSIV = dialog.findViewById(R.id.imgPCar)
        nombretv = dialog.findViewById(R.id.nombrePCar)
        descripciontv = dialog.findViewById(R.id.descripcionPCar)
        preciotv = dialog.findViewById(R.id.precioPCar)
        prefinaltv = dialog.findViewById(R.id.prefinalPCar)
        btnDisminuir = dialog.findViewById(R.id.btnDismi)
        cantidadtv = dialog.findViewById(R.id.cantidadPCar)
        btnAumentar = dialog.findViewById(R.id.btnAum)
        btnAgregarCarrito = dialog.findViewById(R.id.btnAgregarCarrito)

       // datos del modelo
        val productoId = producto.id
        val nombre = producto.titulo
        val descripcion = producto.descripcion
        val precio = producto.precio

        nombretv.setText(nombre)
        descripciontv.setText(descripcion)
        preciotv.text = String.format("S/. %.2f", precio)

        dialog.show()
        dialog.setCanceledOnTouchOutside(true)


    }

    override fun getItemCount(): Int = listaProductos.size

    inner class ProductApiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivImagen: ImageView = itemView.findViewById(R.id.ivProducto)
        val tvProducto: TextView = itemView.findViewById(R.id.Tv_Producto)
        val tvPrecio: TextView = itemView.findViewById(R.id.Tv_Precio)
        val tvDescrip: TextView = itemView.findViewById(R.id.Tv_Descripcion)

        // Si no usas el botón, puedes omitirlo, pero no rompe dejarlo
        val btnComprar = itemView.findViewById<MaterialButton>(R.id.btnComprar)
    }
}