package com.cibertec.proyectogrupo4_dami.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.cibertec.proyectogrupo4_dami.R
import com.cibertec.proyectogrupo4_dami.adapter.CarruselAdapter
import com.cibertec.proyectogrupo4_dami.entity.Usuario
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlin.math.abs

class AccesoActivity : AppCompatActivity() {
    private val listaUsuarios = mutableListOf(
        Usuario(1, "Juan", "Pérez", "juan@cibertec.edu.pe", "1234", "987654321", "71234567")
    )
    private lateinit var vpCarrusel: ViewPager2
    private lateinit var tietCorreologin: TextInputEditText
    private lateinit var tietClavelogin: TextInputEditText
    private lateinit var btnIniciarSesion: Button
    private lateinit var btnRegistrarse: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_acceso)


        vpCarrusel = findViewById(R.id.vpCarrusel)
        tietCorreologin = findViewById(R.id.tietCorreologin)
        tietClavelogin = findViewById(R.id.tietClavelogin)
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion)
        btnRegistrarse = findViewById(R.id.btnRegistrarse)


        configurarCarrusel()

        // Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


    }
    private fun configurarCarrusel() {
        val imagenesCarrusel = listOf(R.drawable.carrusel_03, R.drawable.carrusel_04, R.drawable.carrusel_08,R.drawable.carrusel_07)
        val adapter = CarruselAdapter(imagenesCarrusel)
        vpCarrusel.adapter = adapter

        // Transformación suave
        vpCarrusel.setPageTransformer { page, position ->
            page.apply {
                alpha = 1 - abs(position)
                scaleX = 0.95f + (1 - abs(position)) * 0.05f
                scaleY = 0.95f + (1 - abs(position)) * 0.05f
            }
        }

        // Cambio automático
        Handler(Looper.getMainLooper()).postDelayed(object : Runnable {
            override fun run() {
                val nextItem = (vpCarrusel.currentItem + 1) % imagenesCarrusel.size
                vpCarrusel.setCurrentItem(nextItem, true)
                Handler(Looper.getMainLooper()).postDelayed(this, 3000)
            }
        }, 3000)
    }

}