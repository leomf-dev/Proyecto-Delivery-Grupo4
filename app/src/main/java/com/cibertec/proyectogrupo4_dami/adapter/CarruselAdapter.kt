package com.cibertec.proyectogrupo4_dami.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.cibertec.proyectogrupo4_dami.R

class CarruselAdapter(private val imagenes: List<Int>) : RecyclerView.Adapter<CarruselAdapter.CarruselViewHolder>() {

    inner class CarruselViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgItem: ImageView = view.findViewById(R.id.imgCarrusel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarruselViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_carrusel, parent, false)
        return CarruselViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarruselViewHolder, position: Int) {
        holder.imgItem.setImageResource(imagenes[position])
    }

    override fun getItemCount(): Int = imagenes.size
}