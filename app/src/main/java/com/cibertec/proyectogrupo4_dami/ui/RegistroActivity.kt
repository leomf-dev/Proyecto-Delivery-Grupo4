package com.cibertec.proyectogrupo4_dami.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cibertec.proyectogrupo4_dami.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class RegistroActivity : AppCompatActivity() {
    private lateinit var tietNombreUsuario: TextInputEditText
    private lateinit var tietCorreo: TextInputEditText
    private lateinit var tietTelefono: TextInputEditText
    private lateinit var tietClave: TextInputEditText
    private lateinit var tietConfirmarClave: TextInputEditText
    private lateinit var btnRegistrarse: Button
    private lateinit var btnRegresar: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)


        tietNombreUsuario = findViewById(R.id.tietNombreUsuario)
        tietCorreo = findViewById(R.id.tietCorreo)
        tietTelefono = findViewById(R.id.tietTelefono)
        tietClave = findViewById(R.id.tietClave)
        tietConfirmarClave = findViewById(R.id.tietConfirmarClave)
        btnRegistrarse = findViewById(R.id.btnRegistrarse)
        btnRegresar = findViewById(R.id.btnRegresar)

        // Volver a AccesoActivity
        btnRegresar.setOnClickListener {
            finish()
        }

        // Registrar usuario
        btnRegistrarse.setOnClickListener {
            validarYRegistrar()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun validarYRegistrar() {
        val nombre = tietNombreUsuario.text.toString().trim()
        val correo = tietCorreo.text.toString().trim()
        val telefono = tietTelefono.text.toString().trim()
        val clave = tietClave.text.toString().trim()
        val confirmarClave = tietConfirmarClave.text.toString().trim()


        //Usuario
        if (nombre.isEmpty()) {
            Toast.makeText(this, "Ingresa tu nombre de usuario", Toast.LENGTH_SHORT).show()
            return
        }

        //Correo
        if (correo.isEmpty()) {
            Toast.makeText(this, "Ingresa tu correo", Toast.LENGTH_SHORT).show()
            return
        }
        if (!correo.endsWith("@gmail.com")) {
            Toast.makeText(this, "El correo debe ser @gmail.com", Toast.LENGTH_SHORT).show()
            return
        }
        if (correo.indexOf('@') < 6) {
            Toast.makeText(this, "El correo debe tener al menos 6 caracteres antes de @gmail.com", Toast.LENGTH_SHORT).show()
            return
        }

        //Telefono
        if (telefono.isEmpty()) {
            Toast.makeText(this, "Ingresa tu número de teléfono", Toast.LENGTH_SHORT).show()
            return
        }
        if (telefono.length != 9 || !telefono.all { it.isDigit() }) {
            Toast.makeText(this, "El teléfono debe tener 9 dígitos", Toast.LENGTH_SHORT).show()
            return
        }

        // Contraseña
        if (clave.isEmpty()) {
            Toast.makeText(this, "Ingresa una contraseña", Toast.LENGTH_SHORT).show()
            return
        }
        if (clave.length < 5) {
            Toast.makeText(this, "La contraseña debe tener al menos 5 caracteres", Toast.LENGTH_SHORT).show()
            return
        }
        if (!clave.any { it.isUpperCase() }) {
            Toast.makeText(this, "La contraseña debe contener al menos una letra mayúscula", Toast.LENGTH_SHORT).show()
            return
        }
        if (!clave.any { it.isDigit() }) {
            Toast.makeText(this, "La contraseña debe contener al menos un número", Toast.LENGTH_SHORT).show()
            return
        }

        //Confirmación
        if (confirmarClave.isEmpty()) {
            Toast.makeText(this, "Confirma tu contraseña", Toast.LENGTH_SHORT).show()
            return
        }
        if (clave != confirmarClave) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return
        }

        // Registro exitoso
        val prefs = getSharedPreferences("DatosUsuario", Context.MODE_PRIVATE)
        with(prefs.edit()) {
            putString("nombres", nombre)
            putString("correo", correo)
            putString("clave", clave)
            putString("celular", telefono)
            apply()
        }

        Toast.makeText(this, "¡Te has registrado correctamente!", Toast.LENGTH_LONG).show()

        tietNombreUsuario.setText("")
        tietCorreo.setText("")
        tietTelefono.setText("")
        tietClave.setText("")
        tietConfirmarClave.setText("")

        startActivity(Intent(this, AccesoActivity::class.java))
        finish()
    }
}