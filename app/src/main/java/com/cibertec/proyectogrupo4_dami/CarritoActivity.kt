package com.cibertec.proyectogrupo4_dami

import android.media.tv.TvView
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cibertec.proyectogrupo4_dami.adapter.CarritoAdapter
import com.cibertec.proyectogrupo4_dami.entity.Carrito
import com.cibertec.proyectogrupo4_dami.entity.Producto

class CarritoActivity : AppCompatActivity(){

    private lateinit var rvCarrito: RecyclerView
    private lateinit var tvTotal: TextView
    private lateinit var btnRealizarPedido: Button
    private lateinit var adapter: CarritoAdapter

    private val ListaCarrito = mutableListOf<Carrito>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)

        rvCarrito = findViewById(R.id.rvCarrito)
        tvTotal = findViewById(R.id.tvTotalGeneral)
        btnRealizarPedido = findViewById(R.id.btnRealizarPedido)

       //Productos ejemplo
            ListaCarrito.add(Carrito(Producto(
                        1,
                        "Hamburguesa Clásica",
                        15.0,
                        R.mipmap.ic_producto_foreground
                    ), 2
                )
            )
            /*listaCarrito.add(Carrito(Producto(2, "Papas Fritas", 8.5, R.drawable), 1))
            listaCarrito.add(Carrito(Producto(3, "Gaseosa", 5.0, R.drawable.gaseosa), 3))*/


        adapter = CarritoAdapter(ListaCarrito) {
            actualizarTotal()
        }

        rvCarrito.layoutManager = LinearLayoutManager(this)
        rvCarrito.adapter = adapter
        actualizarTotal()

        btnRealizarPedido.setOnClickListener {
            //abrir pantalla confirman pedido
        }
    }


        private fun actualizarTotal() {
            val total = ListaCarrito.sumOf { it.subtotal }
            tvTotal.text = "Total: S/. %.2f".format(total)
        }
    }