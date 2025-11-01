package com.cibertec.proyectogrupo4_dami.Fragment

import android.os.Bundle
import android.view.View
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

class CategoriaFragment : Fragment(R.layout.fragment__listarcategoria) {

    private lateinit var rvListCat: RecyclerView
    private val productos = mutableListOf<Producto>()
    private lateinit var adapter: ProductApiAdapter
    private var categoriaSeleccionada = ""

    companion object {
        fun newInstance(categoria: String): CategoriaFragment {
            val fragment = CategoriaFragment()
            val args = Bundle().apply {
                putString("categoria", categoria)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            categoriaSeleccionada = it.getString("categoria", "")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar RecyclerView
        rvListCat = view.findViewById(R.id.rvListCat)
        rvListCat.layoutManager = LinearLayoutManager(requireContext())
        adapter = ProductApiAdapter(requireContext(), productos)
        rvListCat.adapter = adapter

        // Cargar productos y filtrar por categoría
        cargarYFiltrarProductos()
    }

    private fun cargarYFiltrarProductos() {
        FakeStoreApi.apiService.getProducts().enqueue(object : Callback<Map<String, Producto>> {
            override fun onResponse(
                call: Call<Map<String, Producto>>,
                response: Response<Map<String, Producto>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val todosProductos = response.body()!!.values
                    val filtrados = todosProductos.filter { it.categoria == categoriaSeleccionada }
                    productos.clear()
                    productos.addAll(filtrados)
                    adapter.notifyDataSetChanged()
                } else {
                    showError("Error al cargar: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Map<String, Producto>>, t: Throwable) {
                showError("Error de red: ${t.message}")
            }
        })
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}