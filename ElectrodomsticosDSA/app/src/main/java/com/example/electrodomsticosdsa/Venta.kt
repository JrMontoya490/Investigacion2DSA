package com.example.electrodomsticosdsa

data class Venta(
    var id: Int = 0,
    var productoId: Int,
    var cantidad: Int,
    var fecha: String,
    var total: Double
)