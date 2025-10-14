package com.cibertec.proyectogrupo4_dami.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cibertec.proyectogrupo4_dami.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class SeleccionarUbicacionActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var btnConfirmarUbicacion: Button
    private var marcador: Marker? = null
    private var direccionSeleccionada: String? = null

    private val LOCATION_PERMISSION_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleccionar_ubicacion)

        // Referencias
        btnConfirmarUbicacion = findViewById(R.id.btnConfirmarUbicacion)

        // Cargar mapa
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Confirmar ubicación
        btnConfirmarUbicacion.setOnClickListener {
            if (direccionSeleccionada != null && marcador != null) {
                val lat = marcador!!.position.latitude
                val lng = marcador!!.position.longitude

                val resultIntent = Intent().apply {
                    putExtra("direccion_seleccionada", direccionSeleccionada)
                    putExtra("latitud", lat)
                    putExtra("longitud", lng)
                }

                setResult(RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(
                    this,
                    "Mantén presionado el mapa para elegir tu ubicación",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // Ajuste de bordes del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isZoomControlsEnabled = true

        // Centrar en Lima
        val lima = LatLng(-12.0464, -77.0428)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(lima, 12f))

        // Verificar permisos de ubicación
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST
            )
        }

        // Mantener presionado para seleccionar ubicación
        map.setOnMapLongClickListener { latLng ->
            marcador?.remove()
            marcador = map.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("Ubicación seleccionada")
            )

            val geocoder = Geocoder(this, Locale.getDefault())
            val direcciones = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

            if (!direcciones.isNullOrEmpty()) {
                direccionSeleccionada = direcciones[0].getAddressLine(0)
                Toast.makeText(this, "📍 $direccionSeleccionada", Toast.LENGTH_LONG).show()
            }
        }
    }

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
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
            ) {
                map.isMyLocationEnabled = true
            }
        }
    }
}
