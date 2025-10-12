package com.cibertec.proyectogrupo4_dami.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.cibertec.proyectogrupo4_dami.R
import com.cibertec.proyectogrupo4_dami.adapter.CarruselAdapter
import com.google.android.material.textfield.TextInputEditText
import kotlin.math.abs

class AccesoActivity : AppCompatActivity() {

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

        // Ir a RegistroActivity
        btnRegistrarse.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }

        // Iniciar sesión
        btnIniciarSesion.setOnClickListener {
            validarYAcceder()
        }


        // Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun validarYAcceder() {
        val correo = tietCorreologin.text.toString().trim()
        val clave = tietClavelogin.text.toString().trim()

        // Correo
        if (correo.isEmpty()) {
            Toast.makeText(this, "Ingresa tu correo", Toast.LENGTH_SHORT).show()
            return
        }

        // Contraseña
        if (clave.isEmpty()) {
            Toast.makeText(this, "Ingresa tu contraseña", Toast.LENGTH_SHORT).show()
            return
        }


        // Leer datos guardados
        val prefs = getSharedPreferences("DatosUsuario", Context.MODE_PRIVATE)
        val correoGuardado = prefs.getString("correo", "") ?: ""
        val claveGuardada = prefs.getString("clave", "") ?: ""

        //Correo existente
        if (correo != correoGuardado) {
            Toast.makeText(this, "La cuenta no existe", Toast.LENGTH_SHORT).show()
            tietCorreologin.setText("")
            return
        }

        // Contraseña existente
        if (clave != claveGuardada) {
            Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
            tietClavelogin.setText("")
            return
        }


        // AQUI PONES, EL NOMBRE DE TU ACTIVITY DONDE ESTAN LAS CATEGORIAS
        val nombres = prefs.getString("nombres", "") ?: ""
        val celular = prefs.getString("celular", "") ?: ""

        val intent = Intent(this, Categorias_Activity::class.java).apply {
            putExtra("nombres", nombres)
            putExtra("correo", correo)
            putExtra("celular", celular)
            putExtra("clave", clave)
        }
        startActivity(intent)
        finish()
    }


    private fun configurarCarrusel() {
        val imagenesCarrusel = listOf(
            R.drawable.carrusel_03,
            R.drawable.carrusel_04,
            R.drawable.carrusel_08,
            R.drawable.carrusel_07)
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