package com.example.electrodomsticosdsa

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.*

class DBHelper(context: Context) : SQLiteOpenHelper(context, "ventas.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        // Crear las tablas en la base de datos
        db.execSQL("CREATE TABLE productos (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, precio REAL)")
        db.execSQL("CREATE TABLE ventas (id INTEGER PRIMARY KEY AUTOINCREMENT, producto_id INTEGER, cantidad INTEGER, fecha TEXT, total REAL)")
        db.execSQL("CREATE TABLE carrito (id INTEGER PRIMARY KEY AUTOINCREMENT, producto_id INTEGER, cantidad INTEGER)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Si hay una nueva versión de la base de datos, elimina las tablas y las crea de nuevo
        db.execSQL("DROP TABLE IF EXISTS productos")
        db.execSQL("DROP TABLE IF EXISTS ventas")
        db.execSQL("DROP TABLE IF EXISTS carrito")
        onCreate(db)
    }

    // Agregar un producto al carrito
    fun agregarAlCarrito(productoId: Int, cantidad: Int) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("producto_id", productoId)
        values.put("cantidad", cantidad)
        db.insert("carrito", null, values)
        db.close()
    }

    // Obtener los productos en el carrito
    fun obtenerCarrito(): List<CarritoItem> {
        val lista = mutableListOf<CarritoItem>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT carrito.id, carrito.producto_id, carrito.cantidad, productos.nombre, productos.precio FROM carrito INNER JOIN productos ON carrito.producto_id = productos.id", null)
        if (cursor.moveToFirst()) {
            do {
                lista.add(
                    CarritoItem(
                        cursor.getInt(0),  // id del carrito
                        Producto(cursor.getInt(1), cursor.getString(3), cursor.getDouble(4)),  // producto
                        cursor.getInt(2)  // cantidad
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }

    // Actualizar la cantidad de un producto en el carrito
    fun actualizarCantidadCarrito(carritoItemId: Int, nuevaCantidad: Int) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("cantidad", nuevaCantidad)
        db.update("carrito", values, "id = ?", arrayOf(carritoItemId.toString()))
        db.close()
    }

    // Eliminar un producto del carrito
    fun eliminarDelCarrito(carritoItemId: Int) {
        val db = writableDatabase
        db.delete("carrito", "id = ?", arrayOf(carritoItemId.toString()))
        db.close()
    }

    // Vaciar el carrito
    fun vaciarCarrito() {
        val db = writableDatabase
        db.delete("carrito", null, null)
        db.close()
    }

    // Registrar una venta
    fun registrarVenta(producto: Producto, cantidad: Int) {
        val total = producto.precio * cantidad
        val fecha = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        val db = writableDatabase
        val values = ContentValues()
        values.put("producto_id", producto.id)
        values.put("cantidad", cantidad)
        values.put("fecha", fecha)
        values.put("total", total)
        db.insert("ventas", null, values)
        db.close()
    }

    // Obtener las ventas
    fun obtenerVentas(): List<Venta> {
        val lista = mutableListOf<Venta>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ventas", null)
        if (cursor.moveToFirst()) {
            do {
                lista.add(
                    Venta(
                        cursor.getInt(0),  // id
                        cursor.getInt(1),  // producto_id
                        cursor.getInt(2),  // cantidad
                        cursor.getString(3),  // fecha
                        cursor.getDouble(4)  // total
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }

    // Obtener todos los productos
    fun obtenerProductos(): List<Producto> {
        val lista = mutableListOf<Producto>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM productos", null)
        if (cursor.moveToFirst()) {
            do {
                lista.add(
                    Producto(
                        cursor.getInt(0),  // id
                        cursor.getString(1),  // nombre
                        cursor.getDouble(2)  // precio
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }

    // Agregar un producto
    fun agregarProducto(producto: Producto) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("nombre", producto.nombre)
        values.put("precio", producto.precio)
        db.insert("productos", null, values)
        db.close()
    }

    // Actualizar un producto
    fun actualizarProducto(producto: Producto) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("nombre", producto.nombre)
        values.put("precio", producto.precio)
        db.update("productos", values, "id = ?", arrayOf(producto.id.toString()))
        db.close()
    }

    // Eliminar un producto
    fun eliminarProducto(productoId: Int) {
        val db = writableDatabase
        db.delete("productos", "id = ?", arrayOf(productoId.toString()))
        db.close()
    }
}
