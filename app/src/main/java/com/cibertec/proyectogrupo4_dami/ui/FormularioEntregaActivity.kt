package com.cibertec.proyectogrupo4_dami.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cibertec.proyectogrupo4_dami.R
import com.cibertec.proyectogrupo4_dami.data.AppDatabaseHelper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import java.util.*

class FormularioEntregaActivity : AppCompatActivity() {

    // --- Variables UI ---
    private lateinit var tietDireccion: EditText
    private lateinit var tietReferencia: EditText
    private lateinit var rgMetodoPago: RadioGroup
    private lateinit var btnConfirmar: Button
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // --- Variables de ubicación ---
    private var direccionSeleccionada: String? = null
    private var latitudSeleccionada: Double? = null
    private var longitudSeleccionada: Double? = null

    // --- Constantes ---
    private val LOCATION_PERMISSION_REQUEST = 100
    private val AUTOCOMPLETE_REQUEST_CODE = 101
    private val MAPA_REQUEST_CODE = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_formulario_entrega)

        // Inicializar vistas
        tietDireccion = findViewById(R.id.tietDireccion)
        tietReferencia = findViewById(R.id.tietReferencia)
        rgMetodoPago = findViewById(R.id.rgMetodoPago)
        btnConfirmar = findViewById(R.id.btnConfirmar)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // --- Inicializar Google Places ---
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.google_maps_key), Locale.getDefault())
        }

        // --- Evento: abrir buscador predictivo ---
        tietDireccion.setOnClickListener {
            val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .setCountries(listOf("PE")) // restringir a Perú
                .build(this)
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
        }

        // --- Evento: usar ubicación actual ---
        findViewById<Button>(R.id.btnUsarUbicacion).setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
            ) {
                obtenerUbicacion()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST
                )
            }
        }

        // --- Evento: elegir ubicación en mapa ---
        findViewById<Button>(R.id.btnElegirEnMapa).setOnClickListener {
            val intent = Intent(this, SeleccionarUbicacionActivity::class.java)
            startActivityForResult(intent, MAPA_REQUEST_CODE)
        }

        // --- Confirmar pedido ---
        btnConfirmar.setOnClickListener { guardarDireccion() }

        // --- Ajustes de bordes del sistema ---
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // ---------------------------------------------------------
    // MÉTODOS DE UBICACIÓN
    // ---------------------------------------------------------

    @SuppressLint("MissingPermission")
    private fun obtenerUbicacion() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                val geocoder = Geocoder(this, Locale.getDefault())
                val direcciones = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                if (!direcciones.isNullOrEmpty()) {
                    val direccionCompleta = direcciones[0].getAddressLine(0)
                    direccionSeleccionada = direccionCompleta
                    latitudSeleccionada = location.latitude
                    longitudSeleccionada = location.longitude
                    tietDireccion.setText(direccionCompleta)
                    Toast.makeText(this, "Ubicación actual detectada ✅", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "No se pudo obtener la dirección exacta", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Activa tu GPS", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // ---------------------------------------------------------
    // GUARDAR DIRECCIÓN EN BD
    // ---------------------------------------------------------

    private fun guardarDireccion() {
        val direccion = direccionSeleccionada ?: ""
        val referencia = tietReferencia.text.toString().trim()
        val metodoId = rgMetodoPago.checkedRadioButtonId

        if (direccion.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa tu dirección", Toast.LENGTH_SHORT).show()
            return
        }

        if (metodoId == -1) {
            Toast.makeText(this, "Selecciona un método de pago", Toast.LENGTH_SHORT).show()
            return
        }

        val metodoPago = findViewById<RadioButton>(metodoId).text.toString()
        val idUsuario = 1 // Simulado (luego puedes obtenerlo de sesión o base de datos)

        val dbHelper = AppDatabaseHelper(this)
        val db = dbHelper.writableDatabase

        val cursor = db.rawQuery(
            "SELECT id_direccion FROM direccion WHERE id_usuario = ?",
            arrayOf(idUsuario.toString())
        )

        if (cursor.moveToFirst()) {
            db.execSQL(
                """
                UPDATE direccion 
                SET direccion = ?, referencia = ?, latitud = ?, longitud = ?
                WHERE id_usuario = ?
                """.trimIndent(),
                arrayOf(direccion, referencia, latitudSeleccionada, longitudSeleccionada, idUsuario)
            )
            Toast.makeText(this, "Dirección actualizada correctamente", Toast.LENGTH_SHORT).show()
        } else {
            val values = ContentValues().apply {
                put("id_usuario", idUsuario)
                put("direccion", direccion)
                put("referencia", referencia)
                put("latitud", latitudSeleccionada)
                put("longitud", longitudSeleccionada)
            }
            db.insert("direccion", null, values)
            Toast.makeText(this, "Dirección guardada correctamente", Toast.LENGTH_SHORT).show()
        }

        cursor.close()
        db.close()
        finish()
    }

    // ---------------------------------------------------------
    // PERMISOS
    // ---------------------------------------------------------

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            obtenerUbicacion()
        } else {
            Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
        }
    }

    // ---------------------------------------------------------
    // RESULTADOS DE INTENTS (Places / Mapa)
    // ---------------------------------------------------------

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // 🔍 Resultado del buscador predictivo (Google Places)
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                RESULT_OK -> {
                    val place = Autocomplete.getPlaceFromIntent(data!!)
                    direccionSeleccionada = place.address
                    latitudSeleccionada = place.latLng?.latitude
                    longitudSeleccionada = place.latLng?.longitude
                    tietDireccion.setText(place.address)
                    Toast.makeText(this, "📍 ${place.name}", Toast.LENGTH_SHORT).show()
                }

                AutocompleteActivity.RESULT_ERROR -> {
                    val status = Autocomplete.getStatusFromIntent(data!!)
                    Toast.makeText(this, "Error: ${status.statusMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // 🗺️ Resultado del mapa manual
        if (requestCode == MAPA_REQUEST_CODE && resultCode == RESULT_OK) {
            val direccion = data?.getStringExtra("direccion_seleccionada")
            val lat = data?.getDoubleExtra("latitud", 0.0)
            val lon = data?.getDoubleExtra("longitud", 0.0)

            if (!direccion.isNullOrEmpty()) {
                direccionSeleccionada = direccion
                latitudSeleccionada = lat
                longitudSeleccionada = lon
                tietDireccion.setText(direccion)
                Toast.makeText(this, "📍 Ubicación seleccionada en mapa", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
