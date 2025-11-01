package com.cibertec.proyectogrupo4_dami.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cibertec.proyectogrupo4_dami.R
import com.cibertec.proyectogrupo4_dami.Fragment.Inicio_MenuActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AccesoActivity : AppCompatActivity() {

    private lateinit var tietCorreologin: TextInputEditText
    private lateinit var tietClavelogin: TextInputEditText
    private lateinit var btnIniciarSesion: Button
    private lateinit var tvRegistrarse: TextView
    private lateinit var btnRegresar: ImageView
    private lateinit var tvAccesoRepartidor: TextView

    private val auth by lazy { FirebaseAuth.getInstance() }
    private val database by lazy { FirebaseDatabase.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_acceso)

        tietCorreologin = findViewById(R.id.tietCorreologin)
        tietClavelogin = findViewById(R.id.tietClavelogin)
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion)
        tvRegistrarse = findViewById(R.id.btnRegistrarse)
        btnRegresar = findViewById(R.id.btnRegresar)
        tvAccesoRepartidor = findViewById(R.id.tvAccesoRepartidor)

        btnIniciarSesion.setOnClickListener { validarYAccederConFirebase() }

        btnRegresar.setOnClickListener {
            finish()
        }

        tvRegistrarse.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }

        tvAccesoRepartidor.setOnClickListener {
            startActivity(Intent(this, AccesoRepartidorActivity::class.java))
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

        if (correo.isEmpty() || clave.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(correo, clave)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid ?: return@addOnCompleteListener
                    database.reference.child("usuarios").child(uid).get()
                        .addOnSuccessListener { snapshot ->
                            val nombres = snapshot.child("nombres").value?.toString() ?: ""
                            val celular = snapshot.child("celular").value?.toString() ?: ""

                            Toast.makeText(this, "¡Bienvenido, $nombres!", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this, Inicio_MenuActivity::class.java).apply {
                                putExtra("nombres", nombres)
                                putExtra("correo", correo)
                                putExtra("celular", celular)
                            }
                            startActivity(intent)
                            finish()
                        }
                } else {
                    Toast.makeText(
                        this,
                        "Inicio de sesión fallido: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
