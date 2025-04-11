package com.example.electrodomsticosdsa

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class VentasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ventas)

        val listView = findViewById<ListView>(R.id.listVentas)
        val db = DBHelper(this)
        val ventas: List<Venta> = db.obtenerVentas()

        val adaptador = ArrayAdapter(this, android.R.layout.simple_list_item_1,
            ventas.map {
                "ID:${it.id} - Producto:${it.productoId} - Cantidad:${it.cantidad} - Fecha:${it.fecha} - Total:$${it.total}"
            })
        listView.adapter = adaptador
    }
}