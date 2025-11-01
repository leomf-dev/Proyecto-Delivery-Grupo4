package com.cibertec.proyectogrupo4_dami.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cibertec.proyectogrupo4_dami.R
import com.cibertec.proyectogrupo4_dami.data.AppDatabaseHelper
import com.google.android.material.textfield.TextInputEditText

class AccesoRepartidorActivity : AppCompatActivity() {

    private lateinit var tietCorreo: TextInputEditText
    private lateinit var tietClave: TextInputEditText
    private lateinit var btnIniciar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acceso_repartidor)

        tietCorreo = findViewById(R.id.tietCorreoRepartidor)
        tietClave = findViewById(R.id.tietClaveRepartidor)
        btnIniciar = findViewById(R.id.btnIniciarRepartidor)

        btnIniciar.setOnClickListener { validarYAcceder() }
    }

    private fun validarYAcceder() {
        val correo = tietCorreo.text.toString().trim()
        val clave = tietClave.text.toString().trim()

        if (correo.isEmpty() || clave.isEmpty()) {
            Toast.makeText(this, "Completa ambos campos", Toast.LENGTH_SHORT).show()
            return
        }

        val dbHelper = AppDatabaseHelper(this)
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT clave, nombre, celular, id_repartidor FROM repartidor WHERE correo = ?",
            arrayOf(correo)
        )

        if (!cursor.moveToFirst()) {
            Toast.makeText(this, "No existe ese repartidor", Toast.LENGTH_SHORT).show()
            cursor.close()
            db.close()
            return
        }

        val claveGuardada = cursor.getString(0)
        val nombre = cursor.getString(1)
        val celular = cursor.getString(2)
        val idRepartidor = cursor.getInt(3)
        cursor.close()
        db.close()

        if (clave != claveGuardada) {
            Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
            return
        }

        // Acceso correcto
        Toast.makeText(this, "Bienvenido $nombre", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, RepartidorMenuActivity::class.java).apply {
            putExtra("id_repartidor", idRepartidor)
            putExtra("nombre", nombre)
            putExtra("correo", correo)
        }
        startActivity(intent)
        finish()
    }
}
