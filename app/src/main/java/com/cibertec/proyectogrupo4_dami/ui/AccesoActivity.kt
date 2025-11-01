package com.cibertec.proyectogrupo4_dami.ui

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
import com.cibertec.proyectogrupo4_dami.Fragment.Inicio_MenuActivity
import com.cibertec.proyectogrupo4_dami.Fragment.ProductsApiFragment
import com.cibertec.proyectogrupo4_dami.adapter.CarruselAdapter
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlin.math.abs

class AccesoActivity : AppCompatActivity() {

    private lateinit var vpCarrusel: ViewPager2
    private lateinit var tietCorreologin: TextInputEditText
    private lateinit var tietClavelogin: TextInputEditText
    private lateinit var btnIniciarSesion: Button
    private lateinit var btnRegistrarse: Button

    private val auth by lazy { FirebaseAuth.getInstance() }
    private val database by lazy { FirebaseDatabase.getInstance() }

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

        // Iniciar sesión con Firebase
        btnIniciarSesion.setOnClickListener {
            validarYAccederConFirebase()
        }

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

    private fun validarYAccederConFirebase() {
        val correo = tietCorreologin.text.toString().trim()
        val clave = tietClavelogin.text.toString().trim()

        // Validaciones básicas
        if (correo.isEmpty()) {
            Toast.makeText(this, "Ingresa tu correo", Toast.LENGTH_SHORT).show()
            return
        }
        if (clave.isEmpty()) {
            Toast.makeText(this, "Ingresa tu contraseña", Toast.LENGTH_SHORT).show()
            return
        }

        // Iniciar sesión con Firebase Authentication
        auth.signInWithEmailAndPassword(correo, clave)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Login exitoso → obtener datos del usuario desde Realtime Database
                    val uid = auth.currentUser?.uid ?: return@addOnCompleteListener

                    database.reference.child("usuarios").child(uid).get()
                        .addOnSuccessListener { snapshot ->
                            val nombres = snapshot.child("nombres").value?.toString() ?: ""
                            val celular = snapshot.child("celular").value?.toString() ?: ""

                            // Ir al menú principal
                            val intent = Intent(this, Inicio_MenuActivity::class.java).apply {
                                putExtra("nombres", nombres)
                                putExtra("correo", correo)
                                putExtra("celular", celular)
                            }
                            startActivity(intent)
                            finish()
                        }
                        .addOnFailureListener { e ->
                            // Si falla la lectura de DB, igual permitimos el ingreso con datos mínimos
                            val intent = Intent(this, ProductsApiFragment::class.java).apply {
                                putExtra("nombres", "")
                                putExtra("correo", correo)
                                putExtra("celular", "")
                            }
                            startActivity(intent)
                            finish()
                        }
                } else {
                    val errorMessage = task.exception?.message ?: "Error desconocido"
                    Toast.makeText(this, "Inicio de sesión fallido: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            }
    }


    private fun configurarCarrusel() {
        val imagenesCarrusel = listOf(
            R.drawable.carrusel_03,
            R.drawable.carrusel_04,
            R.drawable.carrusel_08,
            R.drawable.carrusel_07
        )
        val adapter = CarruselAdapter(imagenesCarrusel)
        vpCarrusel.adapter = adapter

        vpCarrusel.setPageTransformer { page, position ->
            page.apply {
                alpha = 1 - abs(position)
                scaleX = 0.95f + (1 - abs(position)) * 0.05f
                scaleY = 0.95f + (1 - abs(position)) * 0.05f
            }
        }

        Handler(Looper.getMainLooper()).postDelayed(object : Runnable {
            override fun run() {
                val nextItem = (vpCarrusel.currentItem + 1) % imagenesCarrusel.size
                vpCarrusel.setCurrentItem(nextItem, true)
                Handler(Looper.getMainLooper()).postDelayed(this, 3000)
            }
        }, 3000)
    }
}