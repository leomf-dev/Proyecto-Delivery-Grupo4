package com.cibertec.proyectogrupo4_dami

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2

class AccesoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_acceso)

        // Lista de imágenes
        val imagenesCarrusel = listOf(R.drawable.img_hambur, R.drawable.img_pizza, R.drawable.img_lomo)

// Crear adapter
        val adapter = CarruselAdapter(imagenesCarrusel)



// Conectar ViewPager2
        val viewPager: ViewPager2 = findViewById(R.id.vpCarrusel)
        viewPager.adapter = adapter

//  TRANSICIÓN SUAVE CON FUNDO CRUZADO (CROSSFADE)
        viewPager.setPageTransformer { page, position ->
            page.apply {
                viewPager.setPageTransformer { page, position ->
                    page.apply {
                        alpha = 1 - kotlin.math.abs(position)
                        scaleX = 0.95f + (1 - kotlin.math.abs(position)) * 0.05f
                        scaleY = 0.95f + (1 - kotlin.math.abs(position)) * 0.05f
                    }
                }

// Cambio automático cada 3 segundos
                val handler = Handler(Looper.getMainLooper())
                val runnable = object : Runnable {
                    override fun run() {
                        val nextItem = (viewPager.currentItem + 1) % imagenesCarrusel.size
                        viewPager.setCurrentItem(nextItem, true) // 'true' = con animación
                        handler.postDelayed(this, 3000)
                    }
                }
                handler.postDelayed(runnable, 3000)
            }
        }

                ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                    val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                    v.setPadding(
                        systemBars.left,
                        systemBars.top,
                        systemBars.right,
                        systemBars.bottom
                    )
                    insets
                }
            }
        }