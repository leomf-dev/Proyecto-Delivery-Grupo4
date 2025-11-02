package com.cibertec.proyectogrupo4_dami.ui

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

class RegistroRepartidorActivity : AppCompatActivity() {
    private lateinit var tietNombre: TextInputEditText
    private lateinit var tietCorreo: TextInputEditText
    private lateinit var tietTelefono: TextInputEditText
    private lateinit var tietClave: TextInputEditText
    private lateinit var tietConfirmarClave: TextInputEditText
    private lateinit var btnRegistrar: Button
    private lateinit var btnRegresar: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro_repartidor)


        tietNombre = findViewById(R.id.tietNombreRepartidor)
        tietCorreo = findViewById(R.id.tietCorreoRepartidor)
        tietTelefono = findViewById(R.id.tietTelefonoRepartidor)
        tietClave = findViewById(R.id.tietClaveRepartidor)
        tietConfirmarClave = findViewById(R.id.tietConfirmarClaveRepartidor)
        btnRegistrar = findViewById(R.id.btnRegistrarRepartidor)
        btnRegresar = findViewById(R.id.btnRegresar)


        btnRegresar.setOnClickListener {
            finish()
        }


        btnRegistrar.setOnClickListener {
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
        val nombre = tietNombre.text.toString().trim()
        val correo = tietCorreo.text.toString().trim()
        val telefono = tietTelefono.text.toString().trim()
        val clave = tietClave.text.toString().trim()
        val confirmarClave = tietConfirmarClave.text.toString().trim()


        if (nombre.isEmpty()) {
            Toast.makeText(this, "Ingresa tu nombre completo", Toast.LENGTH_SHORT).show()
            return
        }

        if (correo.isEmpty()) {
            Toast.makeText(this, "Ingresa tu correo", Toast.LENGTH_SHORT).show()
            return
        }
        if (!correo.endsWith("@gmail.com")) {
            Toast.makeText(this, "El correo debe ser @gmail.com", Toast.LENGTH_SHORT).show()
            return
        }

        if (telefono.isEmpty() || telefono.length != 9 || !telefono.all { it.isDigit() }) {
            Toast.makeText(this, "Número de teléfono inválido", Toast.LENGTH_SHORT).show()
            return
        }

        if (clave.isEmpty() || clave.length < 5) {
            Toast.makeText(this, "La contraseña debe tener al menos 5 caracteres", Toast.LENGTH_SHORT).show()
            return
        }

        if (clave != confirmarClave) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return
        }


        val dbHelper = AppDatabaseHelper(this)
        val db = dbHelper.writableDatabase


        val cursor = db.rawQuery("SELECT correo FROM repartidor WHERE correo = ?", arrayOf(correo))
        if (cursor.count > 0) {
            cursor.close()
            db.close()
            Toast.makeText(this, "Este correo ya está registrado", Toast.LENGTH_SHORT).show()
            return
        }
        cursor.close()


        val values = android.content.ContentValues().apply {
            put("nombre", nombre)
            put("correo", correo)
            put("clave", clave)
            put("celular", telefono)
        }

        val newRowId = db.insert("repartidor", null, values)
        db.close()

        if (newRowId == -1L) {
            Toast.makeText(this, "Error al registrar repartidor", Toast.LENGTH_SHORT).show()
            return
        }

        Toast.makeText(this, "¡Repartidor registrado correctamente!", Toast.LENGTH_LONG).show()


        tietNombre.setText("")
        tietCorreo.setText("")
        tietTelefono.setText("")
        tietClave.setText("")
        tietConfirmarClave.setText("")


        startActivity(Intent(this, AccesoRepartidorActivity::class.java))
        finish()
    }
}