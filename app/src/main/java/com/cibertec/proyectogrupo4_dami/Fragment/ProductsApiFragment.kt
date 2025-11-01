package com.cibertec.proyectogrupo4_dami.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cibertec.proyectogrupo4_dami.R
import com.cibertec.proyectogrupo4_dami.adapter.ProductApiAdapter
import com.cibertec.proyectogrupo4_dami.data.FakeStoreApi
import com.cibertec.proyectogrupo4_dami.entity.Producto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductsApiFragment : Fragment(R.layout.fragment_products_api) {

    private lateinit var rvProductos: RecyclerView
    private lateinit var adapter: ProductApiAdapter
    private val todosProductos = mutableListOf<Producto>()
    private var productosMostrados = mutableListOf<Producto>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar RecyclerView
        rvProductos = view.findViewById(R.id.rvProductsApi)
        rvProductos.layoutManager = LinearLayoutManager(requireContext())
        productosMostrados = mutableListOf()
        adapter = ProductApiAdapter(requireContext(),productosMostrados)
        rvProductos.adapter = adapter

        // Cargar todos los productos desde Firebase
        cargarProductosDesdeApi()

        // Configurar las categorías con efecto de zoom y clic
        val contenedorCategorias = view.findViewById<LinearLayout>(R.id.contenedorCategorias)
        for (i in 0 until contenedorCategorias.childCount) {
            val categoriaView = contenedorCategorias.getChildAt(i)
            aplicarEfectoZoom(categoriaView)
        }

        //-------BUSCAR EN LA BARRA DE BUSQUEDA------

        val etBuscar = view.findViewById<EditText>(R.id.buscar)
        etBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                if (query.isNotEmpty()) {
                    val filtrados = todosProductos.filter { producto ->
                        producto.titulo.contains(query, ignoreCase = true) ||
                                producto.descripcion.contains(query, ignoreCase = true)
                    }
                    mostrarProductos(filtrados)
                } else {
                    mostrarProductos(todosProductos)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    //-------------CARGAR PRODUCTOS------------------
    private fun cargarProductosDesdeApi() {
        FakeStoreApi.apiService.getProducts().enqueue(object : Callback<Map<String, Producto>> {
            override fun onResponse(
                call: Call<Map<String, Producto>>,
                response: Response<Map<String, Producto>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    todosProductos.clear()
                    todosProductos.addAll(response.body()!!.values)
                    // Mostrar todos los productos al inicio
                    mostrarProductos(todosProductos)
                } else {
                    showError("Error al cargar: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Map<String, Producto>>, t: Throwable) {
                showError("Error de red: ${t.message}")
            }
        })
    }

    private fun mostrarProductos(productos: List<Producto>) {
        productosMostrados.clear()
        productosMostrados.addAll(productos)
        adapter.notifyDataSetChanged()
    }

    //-------------ZOOM------------------
    @SuppressLint("ClickableViewAccessibility")
    private fun aplicarEfectoZoom(vista: View) {
        vista.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.animate().scaleX(1.1f).scaleY(1.1f)
                        .setDuration(120)
                        .setInterpolator(OvershootInterpolator())
                        .start()
                }
                MotionEvent.ACTION_UP,
                MotionEvent.ACTION_CANCEL -> {
                    v.animate().scaleX(1f).scaleY(1f)
                        .setDuration(150)
                        .setInterpolator(OvershootInterpolator())
                        .start()
                }
            }
            false
        }

        //-------------CLICK PRODUCTOS POR CATEGORIAS------------------

        vista.setOnClickListener {
            val categoria = when (vista.id) {
                R.id.layoutCategoria1 -> "Hamburguesas"
                R.id.layoutCategoria2 -> "Postre"
                R.id.layoutCategoria3 -> "Pizza"
                R.id.layoutCategoria4 -> "Saludable"
                R.id.layoutCategoria5 -> "Bebidas"
                else -> ""
            }
            if (categoria.isNotEmpty()) {
                val filtrados = todosProductos.filter { it.categoria == categoria }
                mostrarProductos(filtrados)
            }
        }
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}