package com.cibertec.proyectogrupo4_dami.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.cibertec.proyectogrupo4_dami.R
import com.cibertec.proyectogrupo4_dami.adapter.CarruselAdapter
import com.cibertec.proyectogrupo4_dami.data.AppDatabaseHelper
import com.cibertec.proyectogrupo4_dami.entity.Usuario
import com.google.android.material.textfield.TextInputEditText
import com.cibertec.proyectogrupo4_dami.ui.InicioFragment
import com.cibertec.proyectogrupo4_dami.Fragment.Inicio_MenuActivity
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

        findViewById<TextView>(R.id.tvAccesoRepartidor).setOnClickListener {
            startActivity(Intent(this, AccesoRepartidorActivity::class.java))
        }

        // Ir al registro del repartidor
        findViewById<Button>(R.id.btnRegistroRepartidor).setOnClickListener {
            startActivity(Intent(this, RegistroRepartidorActivity::class.java))
        }


        // Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                maxOf(systemBars.bottom, imeInsets.bottom)
            )
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


        // Buscar usuario en la base de datos
        val dbHelper = AppDatabaseHelper(this)
        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery(
            "SELECT clave, nombres, celular FROM usuario WHERE correo = ?",
            arrayOf(correo)
        )

        // Si no hay resultados
        if (cursor.count == 0) {
        cursor.close()
        db.close()
        Toast.makeText(this, "La cuenta no existe", Toast.LENGTH_SHORT).show()
        tietCorreologin.setText("")
        return
    }
  
        // Obtener datos del usuario
        cursor.moveToFirst()
        val claveGuardada = cursor.getString(0)
        val nombres = cursor.getString(1)
        val celular = cursor.getString(2)
        cursor.close()
        db.close()

        // Validar Contraseña

        if (clave != claveGuardada) {
            Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
            tietClavelogin.setText("")
            return
        }

        //-----------------------------------------------------CAMBIAR ACITVITY----------------
         // Iniciar sesión
        val intent = Intent(this, Inicio_MenuActivity::class.java).apply {
            putExtra("nombres", nombres)
            putExtra("correo", correo)
            putExtra("celular", celular)
            putExtra("clave", clave)
        }
        startActivity(intent)
        finish()
    }



    //Configurar Carrusel
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



        val dbHelper = AppDatabaseHelper(this)
        val db = dbHelper.writableDatabase

        val values = android.content.ContentValues().apply {
            put("nombre", "Juan Pérez")
            put("correo", "repartidor1@gmail.com")
            put("clave", "12345")
            put("celular", "987654321")
        }
        db.insert("repartidor", null, values)
        db.close()

    }


}