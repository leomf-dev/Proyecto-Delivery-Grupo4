package com.cibertec.proyectogrupo4_dami.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.model.Marker
import com.cibertec.proyectogrupo4_dami.R
import com.cibertec.proyectogrupo4_dami.data.AppDatabaseHelper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class RutaEntregaActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var marcadorRepartidor: Marker? = null
    private var destino: LatLng? = null

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ruta_entrega)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapRuta) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Dirección del cliente (en texto)
        val direccion = intent.getStringExtra("direccion")
        val geocoder = Geocoder(this)
        val results = geocoder.getFromLocationName(direccion ?: "", 1)
        if (!results.isNullOrEmpty()) {
            destino = LatLng(results[0].latitude, results[0].longitude)
        }

        findViewById<Button>(R.id.btnEntregado).setOnClickListener {
            marcarPedidoEntregado()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isZoomControlsEnabled = true

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true

            fusedLocationClient.requestLocationUpdates(
                com.google.android.gms.location.LocationRequest.Builder(
                    com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY,
                    2000L
                ).build(),
                object : com.google.android.gms.location.LocationCallback() {
                    override fun onLocationResult(locationResult: com.google.android.gms.location.LocationResult) {
                        val loc = locationResult.lastLocation ?: return
                        val pos = LatLng(loc.latitude, loc.longitude)
                        marcadorRepartidor?.remove()
                        marcadorRepartidor = map.addMarker(
                            MarkerOptions().position(pos).title("Repartidor 🚴")
                        )

                        // Centra el mapa en la primera posición
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 16f))

                        // Dibuja la línea hacia el destino
                        destino?.let {
                            map.addMarker(MarkerOptions().position(it).title("Cliente 📍"))
                            val ruta = listOf(pos, it)
                            map.addPolyline(
                                com.google.android.gms.maps.model.PolylineOptions()
                                    .addAll(ruta)
                                    .width(6f)
                            )
                        }
                    }
                },
                mainLooper
            )
        }
    }

    private fun marcarPedidoEntregado() {
        val idPedido = intent.getIntExtra("id_pedido", 0)
        val dbHelper = AppDatabaseHelper(this)
        val db = dbHelper.writableDatabase
        db.execSQL("UPDATE pedidos SET estado = 'Entregado' WHERE id_pedido = ?", arrayOf(idPedido))
        Toast.makeText(this, "✅ Pedido entregado con éxito", Toast.LENGTH_LONG).show()
        db.close()
        finish()
    }
}