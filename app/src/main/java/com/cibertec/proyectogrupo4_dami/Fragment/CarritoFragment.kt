package com.cibertec.proyectogrupo4_dami.Fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cibertec.proyectogrupo4_dami.R
import com.cibertec.proyectogrupo4_dami.adapter.CarritoAdapter
import com.cibertec.proyectogrupo4_dami.entity.Carrito
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CarritoFragment : Fragment(R.layout.fragment_carrito) {
    private lateinit var carritoRv: RecyclerView
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var listaCarrito : ArrayList<Carrito>
    private lateinit var carritoAdapter : CarritoAdapter
    private lateinit var btnRealizarPedido : MaterialButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_carrito, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        carritoRv = view.findViewById(R.id.CarritoRv)
        carritoRv.layoutManager = LinearLayoutManager(requireContext())

        listaCarrito = ArrayList()
        carritoAdapter = CarritoAdapter(requireContext(), listaCarrito)
        carritoRv.adapter = carritoAdapter

        firebaseAuth = FirebaseAuth.getInstance()
        cargarProductoCarrito()

        sumaProductos()

        btnRealizarPedido = view.findViewById(R.id.btnRealizarPedido)
        btnRealizarPedido.setOnClickListener {
            realizarPedido()
        }

    }

    private fun realizarPedido() {
        val uid = firebaseAuth.uid ?: return

        val refCarrito = FirebaseDatabase.getInstance().getReference("usuarios")
            .child(uid)
            .child("CarritoCompras")

        val refPedidos = FirebaseDatabase.getInstance().getReference("usuarios")
            .child(uid)
            .child("Pedidos")

        refCarrito.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    Toast.makeText(requireContext(), "El carrito está vacío", Toast.LENGTH_SHORT).show()
                    return
                }

                val pedidoId = refPedidos.push().key!!

                val listaProductos = mutableListOf<Map<String, Any>>()
                var total = 0.0

                // snapshot. = representa el nodo CarritoCompras del usuario en FireBase
                // snapshot.children = devuelve la coleccion. (cada producto del carrito)
                for (productoSnap in snapshot.children) {
                    val producto = productoSnap.getValue(Carrito::class.java)
                    if (producto != null) {
                        val item = mapOf(
                            "nombreProducto" to producto.nombre,
                            "cantidad" to producto.cantidad,
                            "precioFinal" to producto.precioFinal
                        )
                        listaProductos.add(item)
                        total += producto.precioFinal.toDouble()
                    }
                }

                val pedido = mapOf(
                    "idPedido" to pedidoId,
                    "fecha" to System.currentTimeMillis(),
                    "estado" to "En camino",
                    "total" to total,
                    "productos" to listaProductos
                )

                // Guardar el pedido
                refPedidos.child(pedidoId).setValue(pedido)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Pedido realizado con éxito", Toast.LENGTH_SHORT).show()

                        // Vaciar el carrito
                        refCarrito.removeValue()

                        // Ir al fragment de pedidos
                        parentFragmentManager.beginTransaction()
                            .replace(R.layout.activity_inicio_menu, PedidosFragment())
                            .addToBackStack(null)
                            .commit()
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Error al registrar el pedido", Toast.LENGTH_SHORT).show()
                    }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun sumaProductos() {
        val ref = FirebaseDatabase.getInstance().getReference("usuarios")
        ref.child(firebaseAuth.uid!!).child("CarritoCompras")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var suma = 0.0
                    for (producto in snapshot.children) {
                        val precioFinal = producto.child("precioFinal").getValue(String::class.java)

                        if (precioFinal != null) {
                            suma += precioFinal.toDouble()
                        }
                    }
                    //  Buscamos el TextView dentro del fragmento
                    val totalGeneral = view?.findViewById<TextView>(R.id.tvTotalGeneral)
                    totalGeneral?.text =  "Total a pagar: S/ %.2f".format(suma)
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    private fun cargarProductoCarrito() {
        val ref = FirebaseDatabase.getInstance().getReference("usuarios")
        ref.child(firebaseAuth.uid!!).child("CarritoCompras")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    listaCarrito.clear()
                    for (ds in snapshot.children) {
                        val carrito = ds.getValue(Carrito::class.java)
                        if (carrito != null) {
                            listaCarrito.add(carrito)
                        }
                    }
                    carritoAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }


}