package com.cibertec.proyectogrupo4_dami.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Context
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.cibertec.proyectogrupo4_dami.R
import com.cibertec.proyectogrupo4_dami.entity.Carrito
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CarritoAdapter(
    private val context: Context,
    private val listaCarrito: List<Carrito>) :
    RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_carrito, parent, false)
        return CarritoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listaCarrito.size
    }

    override fun onBindViewHolder(holder: CarritoViewHolder, position: Int) {
        val Carrito = listaCarrito[position]

        val nombre = Carrito.nombre
        val cantidad = Carrito.cantidad
        val precio = Carrito.precio

       //asignar valores
        holder.tvNombreC.text = nombre
        holder.tvCantidad.text = cantidad.toString()
        holder.tvPrecioC.text = "S/ %.2f".format(precio)

        // Calcular y mostrar subtotal
        val subtotal = precio * cantidad
        holder.tvSubtotalC.text = "Subtotal: S/ %.2f".format(subtotal)



        cargarImagen(Carrito, holder)



//        //MOSTRAR LISTADO PRODUCTO "FIREBASE"
//        Glide.with(holder.itemView.context)
//            .load(item.producto.imagen)
//            .into(holder.imgProducto)
//
//        holder.tvNombre.text = item.producto.titulo
//        holder.tvPrecio.text = "S/ %.2f".format(item.producto.precio)
//        holder.tvCantidad.text = item.cantidad.toString()
//        holder.tvSubtotal.text = "Subtotal: S/ %.2f".format(item.subtotal)
//
//
//        //Aumentar cantidad
//        holder.btnAumentar.setOnClickListener {
//            item.cantidad++
//            notifyItemChanged(position)
//            actualizarTotal()
//        }
//
//        //Disminuir cantidad
//        holder.btnDisminuir.setOnClickListener {
//            if (item.cantidad > 1) {
//                item.cantidad--
//                notifyItemChanged(position)
//                actualizarTotal()
//            }
//        }
//
//        // Eliminar producto
//        holder.btnEliminar.setOnClickListener {
//            listaCarrito.removeAt(position)
//            notifyItemRemoved(position)
//            actualizarTotal()
//        }
    }

    private fun cargarImagen(carrito: Carrito, holder: CarritoAdapter.CarritoViewHolder) {
        val idProducto = carrito.idProducto
        val ref = FirebaseDatabase.getInstance().getReference("products")
        val idFirebase = "prod_$idProducto" // 🔹 mismo formato que usas en el diálogo

        ref.child(idFirebase).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val imagen = snapshot.child("imagen").getValue(String::class.java)
                    Glide.with(holder.itemView.context)
                        .load(imagen)
                        .placeholder(R.mipmap.ic_producto)
                        .transform(CircleCrop())
                        .error(R.mipmap.ic_producto)
                        .into(holder.imgProductoC)
                } else {
                    Glide.with(holder.itemView.context)
                        .load(R.mipmap.ic_producto)
                        .transform(CircleCrop())
                        .into(holder.imgProductoC)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Puedes dejarlo vacío o mostrar un log
            }
        })
    }


    inner class CarritoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgProductoC: ImageView = itemView.findViewById(R.id.imgProductoC)
        val tvNombreC: TextView = itemView.findViewById(R.id.tvNombreProductoC)
        val tvPrecioC: TextView = itemView.findViewById(R.id.tvPrecioProductoC)
        val tvCantidad: TextView = itemView.findViewById(R.id.tvCantidad)
        val tvSubtotalC: TextView = itemView.findViewById(R.id.tvSubtotalC)
        val btnAumentar: MaterialButton = itemView.findViewById(R.id.btnAumentar)
        val btnDisminuir:  MaterialButton = itemView.findViewById(R.id.btnDisminuir)
        val btnEliminar: ImageButton = itemView.findViewById(R.id.btnEliminar)
    }

//    private fun actualizarTotal() {
//        val total = listaCarrito.sumOf { it.subtotal }
//        onActualizarTotal(total)
//    }
//
//    // 🔹 Función auxiliar para obtener el total (por ejemplo, para enviar al checkout)
//
//    //MODIFICADO PARA DOUBLE
//    //val precioNum = it.producto.precio
//
//    fun calcularTotal(): Double {
//        return listaCarrito.sumOf {
//            val precioNum = it.producto.precio
//            precioNum * it.cantidad
//        }
//    }

}