package com.example.electrodomsticosdsa

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: DBHelper
    private lateinit var adapter: ProductoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dbHelper = DBHelper(this)

        val recyclerView = findViewById<RecyclerView>(R.id.rvProductos)
        adapter = ProductoAdapter(dbHelper.obtenerProductos(), this, dbHelper)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        findViewById<Button>(R.id.btnAgregar).setOnClickListener {
            adapter.mostrarDialogo(null, false)
        }

        findViewById<Button>(R.id.btnVentas).setOnClickListener {
            startActivity(Intent(this, VentasActivity::class.java))
        }

        findViewById<Button>(R.id.btnCarrito).setOnClickListener {
            startActivity(Intent(this, CarritoActivity::class.java))
        }
    }
}