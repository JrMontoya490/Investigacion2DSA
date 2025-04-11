package com.example.electrodomsticosdsa

import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CarritoActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var adapter: CarritoAdapter
    private lateinit var rvCarrito: RecyclerView
    private lateinit var txtTotal: TextView
    private lateinit var btnFinalizarCompra: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)

        dbHelper = DBHelper(this)
        rvCarrito = findViewById(R.id.rvCarrito)
        txtTotal = findViewById(R.id.txtTotalCarrito)
        btnFinalizarCompra = findViewById(R.id.btnFinalizarCompra)

        mostrarCarrito()

        btnFinalizarCompra.setOnClickListener {
            // Aquí implementarías la lógica para finalizar la compra
            val carritoItems = dbHelper.obtenerCarrito()
            if (carritoItems.isNotEmpty()) {
                for (item in carritoItems) {
                    dbHelper.registrarVenta(item.producto, item.cantidad)
                }
                dbHelper.vaciarCarrito()
                mostrarCarrito() // Actualizar la vista del carrito
                // Opcional: Mostrar un mensaje de éxito
            } else {
                // Opcional: Mostrar un mensaje de que el carrito está vacío
            }
        }
    }

    private fun mostrarCarrito() {
        val carritoItems = dbHelper.obtenerCarrito()
        adapter = CarritoAdapter(carritoItems, this, dbHelper)
        rvCarrito.layoutManager = LinearLayoutManager(this)
        rvCarrito.adapter = adapter
        actualizarTotal(carritoItems)
    }

    fun actualizarTotal(items: List<CarritoItem>) {
        val total = items.sumOf { it.producto.precio * it.cantidad }
        txtTotal.text = "Total: $${String.format("%.2f", total)}"
    }
}