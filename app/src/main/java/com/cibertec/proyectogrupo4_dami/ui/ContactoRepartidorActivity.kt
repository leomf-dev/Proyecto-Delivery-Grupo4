package com.cibertec.proyectogrupo4_dami.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.Gravity
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.airbnb.lottie.LottieAnimationView
import com.cibertec.proyectogrupo4_dami.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ContactoRepartidorActivity : AppCompatActivity() {

    private lateinit var ivAtras: ImageView
    private lateinit var chat: LinearLayout
    private lateinit var etMensaje: EditText
    private lateinit var ivEnviar: LottieAnimationView

    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private var nombreUsuario: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_contacto_repartidor)

        chat = findViewById(R.id.chat)
        etMensaje = findViewById(R.id.etMensaje)
        ivEnviar = findViewById(R.id.ivEnviar)
        ivAtras = findViewById(R.id.ivAtras)

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference("usuarios")

        obtenerNombreUsuario()

        ivAtras.setOnClickListener {
            finish()
        }

        ivEnviar.addAnimatorListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                ivEnviar.progress = 0f
            }
        })

        ivEnviar.setOnClickListener {
            ivEnviar.playAnimation()
            val texto = etMensaje.text.toString().trim()
            if (texto.isNotEmpty()) {
                agregarMensaje(texto, true)
                etMensaje.text.clear()

                // Respuesta simulada del repartidor
                chat.postDelayed({
                    val nombre = nombreUsuario ?: "cliente"
                    agregarMensaje("Hola $nombre, ya casi llego a tu ubicacion", false)
                }, 1000)
            }
        }

        // Ajustar padding cuando aparece el teclado
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

    private fun obtenerNombreUsuario() {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            dbRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        nombreUsuario = snapshot.child("nombres").value?.toString()
                        agregarMensaje(
                            "Hola ${nombreUsuario ?: "cliente"}, estoy llegando a tu ubicacion",
                            false
                        )
                    } else {
                        nombreUsuario = "cliente"
                        agregarMensaje("Hola, estoy llegando a tu ubicación", false)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    nombreUsuario = "cliente"
                    agregarMensaje("Hola, estoy llegando a tu ubicación", false)
                }
            })
        } else {
            nombreUsuario = "cliente"
            agregarMensaje("Hola, estoy llegando a tu ubicación", false)
        }
    }

    private fun agregarMensaje(msg: String, esUser: Boolean) {
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
            ).apply {
                setMargins(8, 8, 8, 8)
                gravity = if (esUser) Gravity.END else Gravity.START
            }
            layoutParams = params
        }

        chat.addView(tv)

        val scroll = findViewById<ScrollView>(R.id.scrollChat)
        scroll.post { scroll.fullScroll(ScrollView.FOCUS_DOWN) }
    }
}