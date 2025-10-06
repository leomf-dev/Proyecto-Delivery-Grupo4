package com.cibertec.proyectogrupo4_dami

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import entity.Producto

class Categoria_1 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_categoria1)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ////------------------ INICIALIZAR -----------------------
        val layoutCategoria1 = findViewById<LinearLayout>(R.id.layoutCategoria1)
        val layoutCategoria2 = findViewById<LinearLayout>(R.id.layoutCategoria2)
        val layoutCategoria3 = findViewById<LinearLayout>(R.id.layoutCategoria3)
        val layoutCategoria4 = findViewById<LinearLayout>(R.id.layoutCategoria4)
        val layoutCategoria5 = findViewById<LinearLayout>(R.id.layoutCategoria5)
        val layoutCategoria6 = findViewById<LinearLayout>(R.id.layoutCategoria6)
        val layoutCategoria7 = findViewById<LinearLayout>(R.id.layoutCategoria7)
        val layoutCategoria8 = findViewById<LinearLayout>(R.id.layoutCategoria8)
        val layoutCategoria9 = findViewById<LinearLayout>(R.id.layoutCategoria9)
        val layoutCategoria10 = findViewById<LinearLayout>(R.id.layoutCategoria10)

        //------------------------------------------------------

        //------------------ APLICAR EFECTO -----------------------

        aplicarEfectoZoom(layoutCategoria1)
        aplicarEfectoZoom(layoutCategoria2)
        aplicarEfectoZoom(layoutCategoria3)
        aplicarEfectoZoom(layoutCategoria4)
        aplicarEfectoZoom(layoutCategoria5)
        aplicarEfectoZoom(layoutCategoria6)
        aplicarEfectoZoom(layoutCategoria7)
        aplicarEfectoZoom(layoutCategoria8)
        aplicarEfectoZoom(layoutCategoria9)
        aplicarEfectoZoom(layoutCategoria10)

        //------------------------------------------------------

        //------------------ APLICAR POPUP -----------------------

        layoutCategoria1.setOnClickListener {
            val producto = Producto(
                R.mipmap.ic_hamburguesa1,
                "Hamburguesa Clásica",
                "Hamburguesa con queso, lechuga, tomate y salsa especial.",
                "S/ 10.12"
            )
            mostrarPopupProducto(producto)
        }

        layoutCategoria2.setOnClickListener {
            val producto = Producto(
                R.mipmap.ic_hamburguesa2,
                "Doble Carne",
                "Hamburguesa doble carne con salsa BBQ.",
                "S/ 20.10"
            )
            mostrarPopupProducto(producto)
        }

        layoutCategoria3.setOnClickListener {
            val producto = Producto(
                R.mipmap.ic_hamburguesa3,
                "Pollo",
                "Hamburguesa de pollo con aderezo especial.",
                "S/ 16.12"
            )
            mostrarPopupProducto(producto)
        }

        layoutCategoria4.setOnClickListener {
            val producto = Producto(
                R.mipmap.ic_hamburguesa4,
                "Veganas",
                "Hamburguesa 100% vegetal con ingredientes frescos.",
                "S/ 20.12"
            )
            mostrarPopupProducto(producto)
        }

        layoutCategoria5.setOnClickListener {
            val producto = Producto(
                R.mipmap.ic_hamburguesa5,
                "BBQ",
                "Hamburguesa con salsa BBQ y cebolla caramelizada.",
                "S/ 30.12"
            )
            mostrarPopupProducto(producto)
        }

        layoutCategoria6.setOnClickListener {
            val producto = Producto(
                R.mipmap.ic_hamburguesa6,
                "Con Tocino",
                "Hamburguesa con tocino crujiente y queso cheddar.",
                "S/ 20.42"
            )
            mostrarPopupProducto(producto)
        }

        layoutCategoria7.setOnClickListener {
            val producto = Producto(
                R.mipmap.ic_hamburguesa7,
                "Picantes",
                "Hamburguesa con jalapeños y salsa picante especial.",
                "S/ 30.12"
            )
            mostrarPopupProducto(producto)
        }

        layoutCategoria8.setOnClickListener {
            val producto = Producto(
                R.mipmap.ic_hamburguesa8,
                "Especiales de la Casa",
                "Hamburguesa única con ingredientes gourmet.",
                "S/ 23.12"
            )
            mostrarPopupProducto(producto)
        }

        layoutCategoria9.setOnClickListener {
            val producto = Producto(
                R.mipmap.ic_hamburguesa9,
                "De Queso Extra",
                "Hamburguesa con triple queso fundido.",
                "S/ 14.12"
            )
            mostrarPopupProducto(producto)
        }

        layoutCategoria10.setOnClickListener {
            val producto = Producto(
                R.mipmap.ic_hamburguesa10,
                "Mini Burgers",
                "Pequeñas hamburguesas perfectas para compartir.",
                "S/ 6.12"
            )
            mostrarPopupProducto(producto)
        }

    }//FIN

    //------------------------------------------------------

    // -------------Función de zoom reutilizable--------------
    private fun aplicarEfectoZoom(vista: View) {
        vista.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.animate().scaleX(1.1f).scaleY(1.1f).setDuration(150).start()
                    v.elevation = 10f
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    v.animate().scaleX(1f).scaleY(1f).setDuration(150).start()
                    v.elevation = 0f
                }
            }
            false
        }
    }

    //-----------MOSTRAR VENTANA EMERGENTE-----------------
    private fun mostrarPopupProducto(producto: Producto) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.popup_categoria)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val imgProducto = dialog.findViewById<ImageView>(R.id.imgProducto)
        val txtTitulo = dialog.findViewById<TextView>(R.id.txtTitulo)
        val txtDescripcion = dialog.findViewById<TextView>(R.id.txtDescripcion)
        val txtPrecio = dialog.findViewById<TextView>(R.id.txtPrecio)

        imgProducto.setImageResource(producto.imagenResId)
        txtTitulo.text = producto.titulo
        txtDescripcion.text = producto.descripcion
        txtPrecio.text = producto.precio

        dialog.findViewById<Button>(R.id.btnCerrar).setOnClickListener { dialog.dismiss() }
        dialog.findViewById<Button>(R.id.btnSeguirComprando).setOnClickListener { dialog.dismiss() }
        dialog.findViewById<Button>(R.id.btnAgregarCarro).setOnClickListener {
                // LOGICA PARA EL CARRITO DE COMPRAS
            dialog.dismiss()
        }
        dialog.show()
    }


}