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


class ProductoAdapter(
    private var lista: List<Producto>,
    private val context: Context,
    private val dbHelper: DBHelper
) : RecyclerView.Adapter<ProductoAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtProducto: TextView = view.findViewById(R.id.txtProducto)
        val btnEditar: Button = view.findViewById(R.id.btnEditar)
        val btnEliminar: Button = view.findViewById(R.id.btnEliminar)
        val btnAgregarCarrito: Button = view.findViewById(R.id.btnAgregarCarrito) // Cambiado el nombre
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val producto = lista[position]
        holder.txtProducto.text = "${producto.nombre} - $${producto.precio}"

        holder.btnEditar.setOnClickListener {
            mostrarDialogo(producto, true)
        }

        holder.btnEliminar.setOnClickListener {
            dbHelper.eliminarProducto(producto.id)
            actualizar()
        }

        holder.btnAgregarCarrito.setOnClickListener { // Listener para el nuevo botón
            val cantidadInput = EditText(context).apply { hint = "Cantidad" }
            val dialog = AlertDialog.Builder(context)
                .setTitle("Agregar al Carrito")
                .setView(cantidadInput)
                .setPositiveButton("Agregar") { _, _ ->
                    val cantidad = cantidadInput.text.toString().toIntOrNull()
                    if (cantidad != null && cantidad > 0) {
                        dbHelper.agregarAlCarrito(producto.id, cantidad)
                        // Opcional: Mostrar un mensaje de confirmación
                    } else {
                        // Opcional: Mostrar un mensaje de error si la cantidad no es válida
                    }
                }
                .setNegativeButton("Cancelar", null)
                .create()
            dialog.show()
        }
    }

    fun actualizar() {
        lista = dbHelper.obtenerProductos()
        notifyDataSetChanged()
    }

    fun mostrarDialogo(producto: Producto?, esEditar: Boolean) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_producto, null)
        val nombreInput: EditText = dialogView.findViewById(R.id.nombreInput)
        val precioInput: EditText = dialogView.findViewById(R.id.precioInput)

        if (esEditar && producto != null) {
            nombreInput.setText(producto.nombre)
            precioInput.setText(producto.precio.toString())
        }

        val dialog = AlertDialog.Builder(context)
            .setTitle(if (esEditar) "Editar Producto" else "Agregar Producto")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val nombre = nombreInput.text.toString()
                val precio = precioInput.text.toString().toDoubleOrNull()

                if (nombre.isNotEmpty() && precio != null) {
                    if (esEditar && producto != null) {
                        producto.nombre = nombre
                        producto.precio = precio
                        dbHelper.actualizarProducto(producto)
                    } else {
                        dbHelper.agregarProducto(Producto(nombre = nombre, precio = precio))
                    }
                    actualizar()
                } else {
                    // Opcional: Mostrar mensaje de error si los campos están vacíos o el precio no es válido
                }
            }
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.show()
    }
}