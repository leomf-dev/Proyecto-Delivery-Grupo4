package com.cibertec.proyectogrupo4_dami.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cibertec.proyectogrupo4_dami.R

class FormularioEntregaActivity : AppCompatActivity() {

    private lateinit var tietDireccion: EditText
    private lateinit var tietReferencia: EditText
    private lateinit var rgMetodoPago: RadioGroup
    private lateinit var btnConfirmar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_formulario_entrega)

        tietDireccion = findViewById(R.id.tietDireccion)
        tietReferencia = findViewById(R.id.tietReferencia)
        rgMetodoPago = findViewById(R.id.rgMetodoPago)
        btnConfirmar = findViewById(R.id.btnConfirmar)

        btnConfirmar.setOnClickListener {
            val direccion = tietDireccion.text.toString().trim()
            val referencia = tietReferencia.text.toString().trim()
            val metodoId = rgMetodoPago.checkedRadioButtonId

            if (direccion.isEmpty()) {
                Toast.makeText(this, "Por favor ingresa tu dirección", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (metodoId == -1) {
                Toast.makeText(this, "Selecciona un método de pago", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val metodoPago = findViewById<RadioButton>(metodoId).text.toString()

            // Simular checkout exitoso
            Toast.makeText(
                this,
                "Pedido confirmado\nDirección: $direccion\nPago: $metodoPago",
                Toast.LENGTH_LONG
            ).show()

            // Opcional: Ir a pantalla de resumen o volver al inicio
            startActivity(Intent(this, Categorias_Activity::class.java))
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}