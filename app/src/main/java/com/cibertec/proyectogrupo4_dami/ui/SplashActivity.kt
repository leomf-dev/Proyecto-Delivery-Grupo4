package com.cibertec.proyectogrupo4_dami.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cibertec.proyectogrupo4_dami.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        // Instala el splash screen nativo (solo para la transición inicial)
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        // Ajuste de padding para barras de sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // animación del logo
        val logo = findViewById<ImageView>(R.id.icSplash)
        val slideIn = AnimationUtils.loadAnimation(this, R.anim.deslizar_in)
        logo.startAnimation(slideIn)

        // Espera 2 segundos y abre Activity
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, InicioSesionActivity::class.java))
            finish()
        }, 2000)
    }
}