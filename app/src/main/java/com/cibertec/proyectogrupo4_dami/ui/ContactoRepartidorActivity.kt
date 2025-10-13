package com.cibertec.proyectogrupo4_dami.ui

import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cibertec.proyectogrupo4_dami.R

class ContactoRepartidorActivity : AppCompatActivity() {
    private lateinit var chat : LinearLayout
    private lateinit var etMensaje : EditText
    private lateinit var btnEnviar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_contacto_repartidor)

        chat = findViewById(R.id.chat)
        etMensaje = findViewById(R.id.etMensaje)
        btnEnviar = findViewById(R.id.btnEnviar)

        // mensaje ejemplo
        agregarMensaje("Hola, ando llegando a tu ubicacion", false)

        btnEnviar.setOnClickListener {
            val texto = etMensaje.text.toString().trim()
            if (texto.isNotEmpty()) {
                agregarMensaje(texto, true)
                etMensaje.text.clear()
                // respuesta Ejemplo
                chat.postDelayed({
                    agregarMensaje("Ya llego p", false)
                }, 1000)
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun agregarMensaje(msg : String, esUser : Boolean) {
        val tv = TextView(this).apply {
            text = msg
            textSize = 16f
            setPadding(16, 10, 16, 10)
            background = ContextCompat.getDrawable(
                this@ContactoRepartidorActivity,
                if (esUser) R.drawable.burbuja_usuario else R.drawable.burbuja_repartidor
            )
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 8, 8, 8)
            gravity = if (esUser) Gravity.END else Gravity.START
            layoutParams = params
        }
        chat.addView(tv)

        // val scroll = findViewById<ScrollView>(R.id.scrollChat)
        // scroll.post { scroll.fullScroll(ScrollView.FOCUS_DOWN) }
    }
}