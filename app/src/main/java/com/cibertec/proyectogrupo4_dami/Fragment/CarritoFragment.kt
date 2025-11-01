package com.cibertec.proyectogrupo4_dami.Fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cibertec.proyectogrupo4_dami.R
import com.cibertec.proyectogrupo4_dami.adapter.CarritoAdapter
import com.cibertec.proyectogrupo4_dami.entity.Carrito
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