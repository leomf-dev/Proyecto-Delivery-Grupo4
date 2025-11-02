package com.cibertec.proyectogrupo4_dami.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.cibertec.proyectogrupo4_dami.R
import com.cibertec.proyectogrupo4_dami.entity.Pedido
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetallePedidoDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val pedido = arguments?.getParcelable<Pedido>("pedido")

        val view = layoutInflater.inflate(R.layout.detalle_dialog_pedido, null)
        val tvDetalles = view.findViewById<TextView>(R.id.tvDetallesPedido)

        val detalles = pedido?.listaProductos?.joinToString("\n\n") { p ->
            "• ${p.nombreProducto}\n  Cantidad: ${p.cantidad}\n  Subtotal: S/.${String.format("%.2f", p.precioFinal)}"
        } ?: "Sin productos"

        val fechaFormateada = convertirTimestampAFecha(pedido?.fecha)

        val textoCompleto = """
            Fecha: $fechaFormateada
            Estado: ${pedido?.estado}
            Total: S/.${String.format("%.2f", pedido?.total ?: 0.0)}
            
            Productos: 
            $detalles
        """.trimIndent()

        tvDetalles.text = textoCompleto

        return AlertDialog.Builder(requireContext())
            .setTitle("Detalles del Pedido")
            .setView(view)
            .setPositiveButton("Cerrar") { dialog, _ -> dialog.dismiss() }
            .create()
    }

    companion object {

        fun newInstance(pedido: Pedido): DetallePedidoDialog {
            val args = Bundle().apply {
                putParcelable("pedido", pedido)
            }
            return DetallePedidoDialog().apply {
                arguments = args
            }
        }
    }

    private fun convertirTimestampAFecha(timestamp: Long?): String {
        if (timestamp == null || timestamp == 0L) {
            return "Fecha no disponible"
        }
        val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val fechaDate = Date(timestamp)
        return formato.format(fechaDate)
    }
}