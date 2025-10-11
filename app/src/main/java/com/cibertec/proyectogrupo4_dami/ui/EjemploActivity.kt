package com.cibertec.proyectogrupo4_dami.ui

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cibertec.proyectogrupo4_dami.R

class EjemploActivity : AppCompatActivity() {
    private  lateinit var tvEjemplo: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ejemplo)

        tvEjemplo = findViewById(R.id.tvEjemplo)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val nombres = intent.getStringExtra("nombres") ?: ""
        val correo = intent.getStringExtra("correo") ?: ""
        val celular = intent.getStringExtra("celular") ?: ""

        tvEjemplo.text = "¡Bienvenido, $nombres!\nCorreo: $correo\nTeléfono: $celular"


    }
}