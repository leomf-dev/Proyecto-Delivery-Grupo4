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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // arguments = propiedad del fragment que contiene los datos enviados cuando se creo.
        // y en este caso tiene el Bundle del objeto Pedido (mirar new instance).
        // Aqui se obtiene del Bundle el objeto guardado con la clave 'pedido', y si existe y es de tipo
        // Pedido, guardalo en la variable 'pedido'; si no, asigna null.
        val pedido = arguments?.getSerializable("pedido") as? Pedido

        // Crear vista personalizada para el diálogo
        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.detalle_dialog_pedido, null)

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

//        val primerNombre = pedido?.listaProductos?.firstOrNull()?.nombreProducto
//
//        if (primerNombre != null) {
//            println("El primer producto es: $primerNombre")
//            Log.d("DEBUG","El primer producto es: $primerNombre")
//        } else {
//            Log.d("DEBUG","La lista esta vacio")
//            println("La lista de productos está vacía.")
//        }
//
        Log.d("DEBUG","Texto Final: ${pedido?.listaProductos}")

        return AlertDialog.Builder(requireContext())
            .setTitle("Detalles del Pedido")
            .setView(view)
            .setPositiveButton("Cerrar") { dialog, _ -> dialog.dismiss() }
            .create()
    }

    companion object {
        // newInstance guarda el objeto 'pedido' dentro de un Bundle
        // ese bundle se pasa al fragmento y queda disponible en arguments
        fun newInstance(pedido: Pedido): DetallePedidoDialog {
            val args = Bundle()
            args.putSerializable("pedido", pedido)
            val fragment = DetallePedidoDialog()
            fragment.arguments = args
            return fragment
        }
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