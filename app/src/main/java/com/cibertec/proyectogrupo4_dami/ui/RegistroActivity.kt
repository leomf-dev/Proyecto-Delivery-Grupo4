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
import com.cibertec.proyectogrupo4_dami.data.AppDatabaseHelper
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
            Toast.makeText(
                this,
                "El correo debe tener al menos 6 caracteres antes de @gmail.com",
                Toast.LENGTH_SHORT
            ).show()
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
            Toast.makeText(
                this,
                "La contraseña debe tener al menos 5 caracteres",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (!clave.any { it.isUpperCase() }) {
            Toast.makeText(
                this,
                "La contraseña debe contener al menos una letra mayúscula",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (!clave.any { it.isDigit() }) {
            Toast.makeText(
                this,
                "La contraseña debe contener al menos un número",
                Toast.LENGTH_SHORT
            ).show()
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


        // Guardar en base de datos
        val dbHelper = AppDatabaseHelper(this)
        val db = dbHelper.writableDatabase

        // Verificar si el correo ya existe
        val cursor = db.rawQuery("SELECT correo FROM usuario WHERE correo = ?", arrayOf(correo))
        if (cursor.count > 0) {
            cursor.close()
            db.close()
            Toast.makeText(this, "El correo ya está registrado", Toast.LENGTH_SHORT).show()
            return
        }
        cursor.close()

        // Insertar nuevo usuario
        val values = android.content.ContentValues().apply {
            put("nombres", nombre)
            put("correo", correo)
            put("clave", clave)
            put("celular", telefono)
        }

        val newRowId = db.insert("usuario", null, values)
        db.close()

        if (newRowId == -1L) {
            Toast.makeText(this, "Error al registrar", Toast.LENGTH_SHORT).show()
            return
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