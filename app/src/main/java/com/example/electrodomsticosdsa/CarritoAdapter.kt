package com.example.electrodomsticosdsa

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CarritoAdapter(
    private var lista: List<CarritoItem>,
    private val context: Context,
    private val dbHelper: DBHelper
) : RecyclerView.Adapter<CarritoAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtNombre: TextView = view.findViewById(R.id.txtNombreCarrito)
        val txtPrecio: TextView = view.findViewById(R.id.txtPrecioCarrito)
        val txtCantidad: TextView = view.findViewById(R.id.txtCantidadCarrito)
        val btnAumentar: Button = view.findViewById(R.id.btnAumentarCantidad)
        val btnDisminuir: Button = view.findViewById(R.id.btnDisminuirCantidad)
        val btnEliminar: Button = view.findViewById(R.id.btnEliminarCarritoItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_carrito, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista[position]
        holder.txtNombre.text = item.producto.nombre
        holder.txtPrecio.text = "$${item.producto.precio}"
        holder.txtCantidad.text = item.cantidad.toString()

        holder.btnAumentar.setOnClickListener {
            item.cantidad++
            dbHelper.actualizarCantidadCarrito(item.id, item.cantidad)
            notifyItemChanged(position)
            (context as? CarritoActivity)?.actualizarTotal(lista)
        }

        holder.btnDisminuir.setOnClickListener {
            if (item.cantidad > 1) {
                item.cantidad--
                dbHelper.actualizarCantidadCarrito(item.id, item.cantidad)
                notifyItemChanged(position)
                (context as? CarritoActivity)?.actualizarTotal(lista)
            } else {
                // Opcional: Mostrar un mensaje de que la cantidad no puede ser menor a 1
            }
        }

        holder.btnEliminar.setOnClickListener {
            dbHelper.eliminarDelCarrito(item.id)
            lista = dbHelper.obtenerCarrito()
            notifyDataSetChanged()
            (context as? CarritoActivity)?.actualizarTotal(lista)
        }
    }
}