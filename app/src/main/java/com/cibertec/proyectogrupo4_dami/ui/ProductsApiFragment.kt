package com.cibertec.proyectogrupo4_dami.ui

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

class ProductsApiFragment : Fragment(R.layout.fragment_products_api) {

    private lateinit var rvProductos: RecyclerView
    private val productos = mutableListOf<Producto>()
    private lateinit var adapter: ProductApiAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvProductos = view.findViewById(R.id.rvProductsApi)
        rvProductos.layoutManager = LinearLayoutManager(requireContext())
        adapter = ProductApiAdapter(productos)
        rvProductos.adapter = adapter

        cargarProductosDesdeApi()
    }

    private fun cargarProductosDesdeApi() {
        FakeStoreApi.apiService.getProducts().enqueue(object :
            Callback<List<Producto>> {
            override fun onResponse(call: Call<List<Producto>>, response: Response<List<Producto>>) {
                if (response.isSuccessful && response.body() != null) {
                    productos.clear()
                    productos.addAll(response.body()!!)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<List<Producto>>, t: Throwable) {
                Toast.makeText(requireContext(),
                    "Error al cargar: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}