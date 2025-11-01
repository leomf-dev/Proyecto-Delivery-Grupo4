package com.cibertec.proyectogrupo4_dami.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.cibertec.proyectogrupo4_dami.R
import com.cibertec.proyectogrupo4_dami.adapter.SliderAdapter

class InicioSesionActivity : AppCompatActivity() {

    // Declaración de vistas
    private lateinit var vpSlider: ViewPager2
    private lateinit var llIndicadores: LinearLayout
    private lateinit var btnIniciarSesion: android.widget.Button
    private lateinit var btnRegistrarse: android.widget.Button

    // Lista de imágenes para el slider
    private val sliderImages = listOf(
        R.drawable.slider_1,
        R.drawable.slider_2,
        R.drawable.slider_3,
        R.drawable.slider_4

    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inicio_sesion)

        // Inicializar vistas con findViewById
        vpSlider = findViewById(R.id.vpSlider)
        llIndicadores = findViewById(R.id.llIndicadores)
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion)
        btnRegistrarse = findViewById(R.id.btnRegistrarse)



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configurar el slider
        vpSlider.adapter = SliderAdapter(sliderImages)
        setupIndicators()

        // Actualizar indicadores cuando el usuario cambia de página
        vpSlider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateIndicators(position)
            }
        })

        // Botón "Iniciar Sesión"
        btnIniciarSesion.setOnClickListener {
            startActivity(Intent(this, AccesoActivity::class.java))
            finish()
        }

        // Botón "Registrarse"
        btnRegistrarse.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
            finish()
        }
    }

    private fun setupIndicators() {
        llIndicadores.removeAllViews()
        for (i in sliderImages.indices) {
            val indicator = ImageView(this)
            indicator.setImageResource(R.drawable.ic_indicador_inactivo)
            indicator.layoutParams = LinearLayout.LayoutParams(
                resources.getDimensionPixelSize(R.dimen.indicador_size),
                resources.getDimensionPixelSize(R.dimen.indicador_size)
            ).apply {
                marginStart = resources.getDimensionPixelSize(R.dimen.indicador_margin)
                marginEnd = resources.getDimensionPixelSize(R.dimen.indicador_margin)
            }
            llIndicadores.addView(indicator)
        }
        if (llIndicadores.childCount > 0) {
            (llIndicadores.getChildAt(0) as ImageView).setImageResource(R.drawable.ic_indicador_activo)
        }
    }

    private fun updateIndicators(currentPosition: Int) {
        for (i in 0 until llIndicadores.childCount) {
            val indicator = llIndicadores.getChildAt(i) as ImageView
            indicator.setImageResource(
                if (i == currentPosition) R.drawable.ic_indicador_activo
                else R.drawable.ic_indicador_inactivo
            )
        }
    }
}