package com.cibertec.proyectogrupo4_dami

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cibertec.proyectogrupo4_dami.adapter.HistorialAdapter
import com.google.android.material.button.MaterialButton
import entity.Producto

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
            Producto(R.mipmap.ic_hamburguesa6, "Hamburguesa Clasica", "Recién horneado y crujiente", "S/ 13.00",1),
            Producto(R.mipmap.ic_hamburguesa1, "Hamburguesa Queso", "Recién horneado y crujiente", "S/ 21.50",2),
            Producto(R.mipmap.ic_hamburguesa2, "Hamburguesa Doble Carne", "Recién horneado y crujiente", "S/ 16.00",3),
            Producto(R.mipmap.ic_hamburguesa3, "Hamburguesa con Tocino", "Recién horneado y crujiente", "S/ 36.00",4),
            Producto(R.mipmap.ic_hamburguesa4, "Hamburguesa Vegana", "Recién horneado y crujiente", "S/ 41.00",5),
            Producto(R.mipmap.ic_hamburguesa5, "Hamburguesa Carne Doble", "Recién horneado y crujiente", "S/ 12.00",6)
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
    }

    //--------------POPUP PRODUCTO----------------
    @SuppressLint("InflateParams")
    fun mostrarPopupProductoDemo(producto: Producto) {
        val popupView = LayoutInflater.from(this).inflate(R.layout.popup_categoria, null)

        val imgProducto = popupView.findViewById<ImageView>(R.id.imgProducto)
        val txtTitulo = popupView.findViewById<TextView>(R.id.txtTitulo)
        val txtDescripcion = popupView.findViewById<TextView>(R.id.txtDescripcion)
        val txtPrecio = popupView.findViewById<TextView>(R.id.txtPrecio)
        //val edtCantidad = popupView.findViewById<EditText>(R.id.edtCantidad)
        val btnAgregar = popupView.findViewById<Button>(R.id.btnAgregarCarro)
        val btnSeguir = popupView.findViewById<Button>(R.id.btnSeguirComprando)
        val btnCerrar = popupView.findViewById<Button>(R.id.btnCerrar)

        // --------Asignar datos del producto real-----------
        imgProducto.setImageResource(producto.imagenResId)
        txtTitulo.text = producto.titulo
        txtDescripcion.text = producto.descripcion
        txtPrecio.text = producto.precio

        val dialog = AlertDialog.Builder(this)
            .setView(popupView)
            .create()

        popupView.alpha = 0f
        popupView.scaleX = 0.8f
        popupView.scaleY = 0.8f
        popupView.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(250).start()

        btnCerrar.setOnClickListener { dialog.dismiss() }
        btnSeguir.setOnClickListener { dialog.dismiss() }
        btnAgregar.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}
