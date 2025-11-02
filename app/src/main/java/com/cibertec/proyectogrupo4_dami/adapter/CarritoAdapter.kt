package com.cibertec.proyectogrupo4_dami.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Context
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
    private val listaCarrito: MutableList<Carrito>) :
    RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_carrito, parent, false)
        return CarritoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listaCarrito.size
    }

    var costo : Double = 0.0
    override fun onBindViewHolder(holder: CarritoViewHolder, position: Int) {
        val Carrito = listaCarrito[position]

        val nombre = Carrito.nombre
        var cantidad = Carrito.cantidad
        var precio = Carrito.precio
        var precioFinal = Carrito.precioFinal

        //asignar valores
        holder.tvNombreC.text = nombre
        holder.tvCantidad.text = cantidad.toString()
        holder.tvPrecioC.text = "Precio: S/ %.2f".format(precio)

        // Calcular y mostrar subtotal
        val subtotal = precio * cantidad
        holder.tvSubtotalC.text = "Subtotal: S/ %.2f".format(subtotal)

        cargarImagen(Carrito, holder)

        holder.btnEliminar.setOnClickListener {
            eliminarProdCarrito(context, Carrito.idProducto)
        }

        var PrecioFinalDb = precioFinal.toDouble()

        holder.btnAumentar.setOnClickListener {
            costo = precio.toDouble()
            PrecioFinalDb += costo
            cantidad++

            holder.tvSubtotalC.text = PrecioFinalDb.toString()
            holder.tvCantidad.text = cantidad.toString()

            var precioFinalSt = PrecioFinalDb.toString()

            calcularNuevoPrecio(context, Carrito.idProducto, precioFinalSt, cantidad)
        }

        holder.btnDisminuir.setOnClickListener {
            if (cantidad > 1) {
                costo = precio.toDouble()
                PrecioFinalDb = PrecioFinalDb - costo
                cantidad--

            holder.tvSubtotalC.text = PrecioFinalDb.toString()
            holder.tvCantidad.text = cantidad.toString()

            var precioFinalSt = PrecioFinalDb.toString()
            calcularNuevoPrecio(context, Carrito.idProducto, precioFinalSt, cantidad)
            }
        }
    }

    private fun calcularNuevoPrecio(context: Context, idProducto: String, precioFinalSt: String, cantidad: Int) {
        val hashMap : HashMap<String, Any> = HashMap()
        val firebaseAuth = FirebaseAuth.getInstance()

        hashMap["cantidad"] = cantidad
        hashMap["precioFinal"] =precioFinalSt

        val ref = FirebaseDatabase.getInstance().getReference("usuarios")
        ref.child(firebaseAuth.uid!!).child("CarritoCompras").child(idProducto)
            .updateChildren(hashMap)
            .addOnSuccessListener {
                Toast.makeText(context, "Se actualizo la cantidad", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e->
                Toast.makeText(context, "${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun eliminarProdCarrito(context: Context, idProducto: String) {
        val firebaseAuth = FirebaseAuth.getInstance()

        val ref = FirebaseDatabase.getInstance().getReference("usuarios")
        ref.child(firebaseAuth.uid!!).child("CarritoCompras").child(idProducto)
            .removeValue()
            .addOnSuccessListener {
                Toast.makeText(context, "Se elimino el producto del carrito", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e->
                Toast.makeText(context, "${e.message}", Toast.LENGTH_SHORT).show()

            }
    }




    private fun cargarImagen(carrito: Carrito, holder: CarritoAdapter.CarritoViewHolder) {
        val idProducto = carrito.idProducto
        val ref = FirebaseDatabase.getInstance().getReference("products")
        val idFirebase = "prod_$idProducto"

        ref.child(idFirebase).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val imagen = snapshot.child("imagen").getValue(String::class.java)
                    Glide.with(holder.itemView.context)
                        .load(imagen)
                        .placeholder(R.mipmap.carga_prod)
                        .transform(CircleCrop())
                        .error(R.mipmap.carga_prod)
                        .into(holder.imgProductoC)
                } else {
                    Glide.with(holder.itemView.context)
                        .load(R.mipmap.carga_prod)
                        .transform(CircleCrop())
                        .into(holder.imgProductoC)
                }
            }

            override fun onCancelled(error: DatabaseError) {
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

}
