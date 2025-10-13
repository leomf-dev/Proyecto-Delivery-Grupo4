package com.cibertec.proyectogrupo4_dami.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.cibertec.proyectogrupo4_dami.R
import com.cibertec.proyectogrupo4_dami.entity.Carrito
import com.google.android.material.button.MaterialButton

class CarritoAdapter(
    private val listaCarrito: MutableList<Carrito>,
    private val onActualizarTotal: (Double) -> Unit // se usa para actualizar el total
) : RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder>() {

    inner class CarritoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgProducto: ImageView = itemView.findViewById(R.id.imgProducto)
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombreProducto)
        val tvPrecio: TextView = itemView.findViewById(R.id.tvPrecioProducto)
        val tvCantidad: TextView = itemView.findViewById(R.id.tvCantidad)
        val tvSubtotal: TextView = itemView.findViewById(R.id.tvSubtotal)
        val btnAumentar: MaterialButton = itemView.findViewById(R.id.btnAumentar)
        val btnDisminuir:  MaterialButton = itemView.findViewById(R.id.btnDisminuir)
        val btnEliminar: ImageButton = itemView.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_carrito, parent, false)
        return CarritoViewHolder(view)
    }

    override fun getItemCount(): Int = listaCarrito.size

    override fun onBindViewHolder(holder: CarritoViewHolder, position: Int) {
        val item = listaCarrito[position]

        holder.imgProducto.setImageResource(item.producto.imagenResId)
        holder.tvNombre.text = item.producto.titulo
        holder.tvPrecio.text = item.producto.precio
        holder.tvCantidad.text = item.cantidad.toString()
        holder.tvSubtotal.text = "Subtotal: S/ %.2f".format(item.subtotal)

        //Aumentar cantidad
        holder.btnAumentar.setOnClickListener {
            item.cantidad++
            notifyItemChanged(position)
            actualizarTotal()
        }

        //Disminuir cantidad
        holder.btnDisminuir.setOnClickListener {
            if (item.cantidad > 1) {
                item.cantidad--
                notifyItemChanged(position)
                actualizarTotal()
            }
        }

        // Eliminar producto
        holder.btnEliminar.setOnClickListener {
            listaCarrito.removeAt(position)
            notifyItemRemoved(position)
            actualizarTotal()
        }
    }

    private fun actualizarTotal() {
        val total = listaCarrito.sumOf { it.subtotal }
        onActualizarTotal(total)
    }
}