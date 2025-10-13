package com.cibertec.proyectogrupo4_dami.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cibertec.proyectogrupo4_dami.R
import com.cibertec.proyectogrupo4_dami.adapter.HistorialAdapter
import com.cibertec.proyectogrupo4_dami.entity.Producto
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.ranges.until

class Categorias_Activity : AppCompatActivity() {


    //--------VARIABLES----------
    private lateinit var rvHistorial : RecyclerView
    private lateinit var historialAdapter: HistorialAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_categorias)

        rvHistorial = findViewById(R.id.rvHistorial)



        //----COLOCAR DATOS "REEMPLAZAR POR DATOS DE SQL"
        val productos = listOf(
            Producto(
                R.mipmap.ic_hamburguesa1,
                "Hamburguesa Clasica",
                "Recién horneado y crujiente",
                "S/ 13.00",
                1
            ),
            Producto(
                R.mipmap.ic_hamburguesa2,
                "Hamburguesa Queso",
                "Recién horneado y crujiente",
                "S/ 21.50",
                2
            ),
            Producto(
                R.mipmap.ic_hamburguesa3,
                "Hamburguesa Doble Carne",
                "Recién horneado y crujiente",
                "S/ 16.00",
                3
            ),
            Producto(
                R.mipmap.ic_hamburguesa4,
                "Hamburguesa con Tocino",
                "Recién horneado y crujiente",
                "S/ 36.00",
                4
            ),
            Producto(
                R.mipmap.ic_hamburguesa5,
                "Hamburguesa Vegana",
                "Recién horneado y crujiente",
                "S/ 41.00",
                5
            ),
            Producto(
                R.mipmap.ic_hamburguesa6,
                "Hamburguesa de Pollo",
                "Recién horneado y crujiente",
                "S/ 12.00",
                6
            ),
            Producto(
                R.mipmap.ic_hamburguesa7,
                "Hamburguesa Mini",
                "Recién horneado y crujiente",
                "S/ 12.00",
                6
            ),
            Producto(
                    R.mipmap.ic_hamburguesa8,
            "Hamburguesa BQQ",
            "Recién horneado y crujiente",
            "S/ 12.00",
            6
        )
        )

        //------------APLICA ZOOM GENERAL---------
        val contenedorCategorias = findViewById<LinearLayout>(R.id.contenedorCategorias)

        for (i in 0 until contenedorCategorias.childCount) {
            val categoria = contenedorCategorias.getChildAt(i)
            aplicarEfectoZoom(categoria)
        }

        //------------------------------------------------

        historialAdapter = HistorialAdapter(productos)
        rvHistorial.layoutManager = LinearLayoutManager(this)
        rvHistorial.adapter = historialAdapter


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    //----------METODOS-------------
    @SuppressLint("ClickableViewAccessibility")
    fun aplicarEfectoZoom(vista: View) {
        vista.setOnTouchListener { v, e ->
            when (e.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.animate()
                        .scaleX(1.1f).scaleY(1.1f)
                        .setDuration(120)
                        .setInterpolator(OvershootInterpolator())
                        .start()
                    v.performClick()
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    v.animate()
                        .scaleX(1f).scaleY(1f)
                        .setDuration(150)
                        .setInterpolator(OvershootInterpolator())
                        .start()
                }
            }
            true
        }

        // ---- Botón para ir al formulario de entrega ----
        val fabAgregarDireccion = findViewById<FloatingActionButton>(R.id.fabAgregarDireccion)
        fabAgregarDireccion.setOnClickListener {
            val intent = Intent(this, FormularioEntregaActivity::class.java)
            startActivity(intent)
        }


    }



}


