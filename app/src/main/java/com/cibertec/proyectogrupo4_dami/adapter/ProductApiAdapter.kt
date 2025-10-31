package com.cibertec.proyectogrupo4_dami.adapter

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cibertec.proyectogrupo4_dami.R
import com.cibertec.proyectogrupo4_dami.entity.Producto
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

import com.google.firebase.database.ValueEventListener
import kotlin.math.cos


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

    var costo : Double = 0.0
    var costoFinal : Double = 0.0
    var cantidadProd : Int = 0


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


        costo = precio

        nombretv.setText(nombre)
        descripciontv.setText(descripcion)
        preciotv.text = String.format("S/. %.2f", precio)

        costoFinal = costo
        cantidadProd= 1

        //incrementar cantidad
        btnAumentar.setOnClickListener {
            costoFinal = costoFinal + costo
            cantidadProd++

            prefinaltv.text = String.format("S/. %.2f", costoFinal)
            cantidadtv.text = cantidadProd.toString()
        }

        //Disminuir cantidad
        btnDisminuir.setOnClickListener {
            //disminuir solo si la cantidad es mayor a 1
            if(cantidadProd > 1){
                costoFinal = costoFinal - costo
                cantidadProd--

                prefinaltv.text = String.format("S/. %.2f", costoFinal)
                cantidadtv.text = cantidadProd.toString()
            }
        }
        prefinaltv.text = String.format("S/. %.2f", costoFinal)

        //Cargar la imagen
        cargarimg(productoId, imagenSIV)

        btnAgregarCarrito.setOnClickListener {
            agregarCarrito(context, producto, costoFinal, cantidadProd)
        }



        dialog.show()
        dialog.setCanceledOnTouchOutside(true)
    }


    private fun ProductApiAdapter.agregarCarrito(context: Context, producto: Producto, costoFinal: Double, cantidadProd: Int) {
        val firebaseAuth = FirebaseAuth.getInstance()
        val hashMap = HashMap<String, Any>()
        hashMap["idProducto"] = producto.id
        hashMap["nombre"] = producto.titulo
        hashMap["precio"] = producto.precio
        hashMap["precioFinal"] = costoFinal
        hashMap["cantidad"] = cantidadProd

        val ref = FirebaseDatabase.getInstance().getReference("usuarios")
        ref.child(firebaseAuth.uid!!).child("CarritoCompras").child(producto.id)
            .setValue(hashMap)
            .addOnSuccessListener{
                Toast.makeText(context, "Se agrego el producto al carrito",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e->
                Toast.makeText(context, "${e.message}",Toast.LENGTH_SHORT).show()
            }
    }


    private fun cargarimg(productoId: String, imagenSIV: ShapeableImageView) {
        val ref = FirebaseDatabase.getInstance().getReference("products")
        val idFirebase = "prod_$productoId"

        ref.child(idFirebase).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val imagen = snapshot.child("imagen").value.toString()

                    Glide.with(context)
                        .load(imagen)
                        .placeholder(R.mipmap.ic_producto)
                        .into(imagenSIV)
                }
            }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            } )
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
