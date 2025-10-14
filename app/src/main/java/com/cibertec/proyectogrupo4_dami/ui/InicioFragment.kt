package com.cibertec.proyectogrupo4_dami.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cibertec.proyectogrupo4_dami.R
import com.cibertec.proyectogrupo4_dami.adapter.HistorialAdapter
import com.cibertec.proyectogrupo4_dami.entity.Producto
import com.google.android.material.button.MaterialButton

class InicioFragment : Fragment() {

    //----------------VARIABLES-------------
    private lateinit var rvHistorial: RecyclerView
    private lateinit var historialAdapter: HistorialAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflamos el diseño del fragmento (fragment_inicio.xml)
        return inflater.inflate(R.layout.fragment_inicio, container, false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Conectamos el RecyclerView del XML con el código
        rvHistorial = view.findViewById(R.id.rvHistorial)

        //----------LISTAR---------
        val productos = listOf(
            Producto(
                R.mipmap.ic_hamburguesa6,
                "Hamburguesa Clasica",
                "Recién horneado y crujiente",
                "S/ 13.00",
                1
            ),
            Producto(
                R.mipmap.ic_hamburguesa1,
                "Hamburguesa Queso",
                "Recién horneado y crujiente",
                "S/ 21.50",
                2
            ),
            Producto(
                R.mipmap.ic_hamburguesa2,
                "Hamburguesa Doble Carne",
                "Recién horneado y crujiente",
                "S/ 16.00",
                3
            ),
            Producto(
                R.mipmap.ic_hamburguesa3,
                "Hamburguesa con Tocino",
                "Recién horneado y crujiente",
                "S/ 36.00",
                4
            ),
            Producto(
                R.mipmap.ic_hamburguesa4,
                "Hamburguesa Vegana",
                "Recién horneado y crujiente",
                "S/ 41.00",
                5
            ),
            Producto(
                R.mipmap.ic_hamburguesa5,
                "Hamburguesa Carne Doble",
                "Recién horneado y crujiente",
                "S/ 12.00",
                6
            )
        )

        // Aplica un efecto visual a las categorías (zoom al tocar)
        val contenedorCategorias = view.findViewById<LinearLayout>(R.id.contenedorCategorias)
        for (i in 0 until contenedorCategorias.childCount) {
            val categoria = contenedorCategorias.getChildAt(i)
            aplicarEfectoZoom(categoria)
        }

        // Configuramos el adaptador con los productos y el diseño en lista
        historialAdapter = HistorialAdapter(productos)
        rvHistorial.layoutManager = LinearLayoutManager(requireContext())
        rvHistorial.adapter = historialAdapter

        // ---- Botón para abrir formulario ----
        val btnIrFormulario = view.findViewById<MaterialButton>(R.id.btnIrFormulario)
        btnIrFormulario.setOnClickListener {
            val intent = Intent(requireContext(), FormularioEntregaActivity::class.java)
            startActivity(intent)
        }

    }


    // Función para aplicar efecto de zoom al presionar una categoría
    @SuppressLint("ClickableViewAccessibility")
    private fun aplicarEfectoZoom(vista: View) {
        vista.setOnTouchListener { v, e ->
            when (e.action) {

                // Aumenta ligeramente el tamaño al presionar
                MotionEvent.ACTION_DOWN -> {
                    v.animate().scaleX(1.1f).scaleY(1.1f)
                        .setDuration(120)
                        .setInterpolator(OvershootInterpolator())
                        .start()
                    v.performClick()
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {

                    // Regresa al tamaño normal al soltar
                    v.animate().scaleX(1f).scaleY(1f)
                        .setDuration(150)
                        .setInterpolator(OvershootInterpolator())
                        .start()
                }
            }
            true
        }
    }
}