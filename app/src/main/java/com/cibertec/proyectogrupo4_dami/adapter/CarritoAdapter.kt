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
class CarritoAdapter(
    private val lista: MutableList<Carrito>,
    private val onUpdate: () -> Unit // se usa para actualizar el total
) : RecyclerView.Adapter<CarritoAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgProducto: ImageView = itemView.findViewById(R.id.imgProducto)
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombreProducto)
        val tvPrecio: TextView = itemView.findViewById(R.id.tvPrecioProducto)
        val tvCantidad: TextView = itemView.findViewById(R.id.tvCantidad)
        val tvSubtotal: TextView = itemView.findViewById(R.id.tvSubtotal)
        val btnAumentar: Button = itemView.findViewById(R.id.btnAumentar)
        val btnDisminuir: Button = itemView.findViewById(R.id.btnDisminuir)
        /*val btnEliminar: ImageButton = itemView.findViewById(R.id.btnEliminar)*/
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_carrito, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista[position]

        holder.imgProducto.setImageResource(item.producto.imagen)
        holder.tvNombre.text = item.producto.nombre
        holder.tvPrecio.text = "S/. %.2f".format(item.producto.precio)
        holder.tvCantidad.text = item.cantidad.toString()
        holder.tvSubtotal.text = "Subtotal: S/. %.2f".format(item.subtotal)

        holder.btnAumentar.setOnClickListener {
            item.cantidad++
            holder.tvCantidad.text = item.cantidad.toString()
            holder.tvSubtotal.text = "Subtotal: S/. %.2f".format(item.subtotal)
            onUpdate()
        }

        holder.btnDisminuir.setOnClickListener {
            if (item.cantidad > 1) {
                item.cantidad--
                holder.tvCantidad.text = item.cantidad.toString()
                holder.tvSubtotal.text = "Subtotal: S/. %.2f".format(item.subtotal)
                onUpdate()
            } else {
                Toast.makeText(holder.itemView.context, "Cantidad mínima: 1", Toast.LENGTH_SHORT).show()
            }
        }

       /* holder.btnEliminar.setOnClickListener {
            lista.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, lista.size)
            Toast.makeText(holder.itemView.context, "Producto eliminado", Toast.LENGTH_SHORT).show()
            onUpdate()
        }*/
    }

    override fun getItemCount(): Int = lista.size
}