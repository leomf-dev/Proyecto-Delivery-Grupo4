package com.cibertec.proyectogrupo4_dami

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.cibertec.proyectogrupo4_dami.ui.InicioFragment
import com.cibertec.proyectogrupo4_dami.ui.PedidosFragment
import com.cibertec.proyectogrupo4_dami.ui.PerfilFragment
import com.google.android.material.navigation.NavigationView

class Inicio_MenuActivity : AppCompatActivity() {

    //VARIABLES
    private lateinit var DlayMenu : DrawerLayout
    private lateinit var IvMenu : ImageView
    private lateinit var MenuNavi  : NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_inicio_menu)

        //Inicializar
        DlayMenu = findViewById(R.id.dlayMenu)
        IvMenu = findViewById(R.id.ivMenu)
        MenuNavi = findViewById(R.id.menuNavi)

        //ABRIR CON CLICK
        IvMenu.setOnClickListener {
            DlayMenu.open()
        }

        MenuNavi.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            DlayMenu.closeDrawers()

            //-----LLAMAR A FRAGMENT----
            when (menuItem.itemId){
                R.id.itInicio -> replaceFragment(InicioFragment())
                R.id.itPedidos -> replaceFragment(PedidosFragment())
                R.id.itPerfil -> replaceFragment(PerfilFragment())
            }
            true
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.dlayMenu)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        replaceFragment(InicioFragment())
    }

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.ConteFragment, fragment).commit()
    }
}