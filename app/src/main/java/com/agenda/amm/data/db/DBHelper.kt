package com.agenda.amm.data.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.agenda.amm.data.model.Cliente
import com.agenda.amm.data.model.Empleado
import com.agenda.amm.data.model.Equipo
import com.agenda.amm.data.model.Servicio
import com.agenda.amm.data.model.TipoDocumento
import com.agenda.amm.data.model.TipoLimpieza

class DBHelper(context: Context) : SQLiteOpenHelper(context, "AMMDataBase", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        // tabla de usuarios
        db.execSQL("""
            CREATE TABLE usuarios (
                id_usuario INTEGER PRIMARY KEY AUTOINCREMENT,
                usuario TEXT UNIQUE,
                contrasena TEXT
            )            
        """.trimIndent())

        //tabla tipo_documento
        db.execSQL("""
            CREATE TABLE tipo_documento(
                id_tipoDocu INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre_tipo TEXT UNIQUE NOT NULL
            )
        """.trimIndent())

        //tabla tipo_limpieza
        db.execSQL("""
            CREATE TABLE tipo_limpieza(
                id_tipoLimp INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre_tipo TEXT UNIQUE NOT NULL
            )
        """.trimIndent())

        //tabla cliente
        db.execSQL("""
            CREATE TABLE cliente (
                id_cliente INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre_cliente TEXT NOT NULL,
                apellido_cliente TEXT NOT NULL,
                direccion_cliente TEXT NOT NULL,
                telefono_cliente TEXT NOT NULL,
                correo_cliente TEXT NOT NULL,
                observacion_cliente TEXT
            )
        """.trimIndent())

        //tabla empleado
        db.execSQL("""
            CREATE TABLE empleado (
                id_empleado TEXT PRIMARY KEY,
                id_tipoDocu INTEGER NOT NULL,
                documento_empleado TEXT NOT NULL,
                nombre_empleado TEXT NOT NULL,
                apellido_empleado TEXT NOT NULL,
                telefono_empleado TEXT NOT NULL,
                correo_empleado TEXT NOT NULL,
                FOREIGN KEY (id_tipoDocu) REFERENCES tipo_documento(id_tipoDocu)
            )
        """.trimIndent())

        //tabla equipo
        db.execSQL("""
            CREATE TABLE equipo (
                id_equipo INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre_equipo TEXT NOT NULL,
                lider TEXT NOT NULL,
                miembro1 TEXT NOT NULL,
                miembro2 TEXT NOT NULL,
                FOREIGN KEY (lider) REFERENCES empleado(id_empleado),
                FOREIGN KEY (miembro1) REFERENCES empleado(id_empleado),
                FOREIGN KEY (miembro2) REFERENCES empleado(id_empleado)
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE servicio (
                id_servicio INTEGER PRIMARY KEY AUTOINCREMENT,
                id_cliente INTEGER NOT NULL,
                id_equipo INTEGER NOT NULL,
                id_tipoLimp INTEGER NOT NULL,
                fecha TEXT NOT NULL,
                hora TEXT NOT NULL,
                tiempo TEXT NOT NULL,
                finalizacion TEXT NOT NULL,
                precio INTEGER NOT NULL,
                observacion_servicio TEXT,
                FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente),
                FOREIGN KEY (id_equipo) REFERENCES equipo(id_equipo),
                FOREIGN KEY (id_tipoLimp) REFERENCES tipo_limpieza(id_tipoLimp)
            )
        """.trimIndent())

        db.execSQL("INSERT INTO usuario (usuario, contrasena) VALUES ('admin', '1234')")
        db.execSQL("INSERT INTO tipo_limpieza (nombre_tipo) VALUES ('Estandar')")
        db.execSQL("INSERT INTO tipo_limpieza (nombre_tipo) VALUES ('Profunda')")
        db.execSQL("INSERT INTO tipo_limpieza (nombre_tipo) VALUES ('Move IN/OUT')")
        db.execSQL("INSERT INTO tipo_documento (nombre_tipo) VALUES ('DNI')")
        db.execSQL("INSERT INTO tipo_documento (nombre_tipo) VALUES ('Pasaporte')")
        db.execSQL("INSERT INTO tipo_documento (nombre_tipo) VALUES ('Visa')")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS servicio")
        db.execSQL("DROP TABLE IF EXISTS usuario")
        db.execSQL("DROP TABLE IF EXISTS empleado")
        db.execSQL("DROP TABLE IF EXISTS equipo")
        db.execSQL("DROP TABLE IF EXISTS cliente")
        db.execSQL("DROP TABLE IF EXISTS tipo_documento")
        db.execSQL("DROP TABLE IF EXISTS tipo_limpieza")
        onCreate(db);
    }

    override fun onOpen(db: SQLiteDatabase) {
        super.onOpen(db)
        db.execSQL("PRAGMA foreign_keys=ON;")
    }
    // === Log y Registro ===
    fun registroUsuario(usuario: String, contrasena: String): Boolean {
        val db = readableDatabase
        // verificar si el usuario ya existe
        val cursor = db.rawQuery("SELECT * FROM usuario WHERE usuario = ?", arrayOf(usuario))
        val existe = cursor.moveToFirst()
        cursor.close()
        //si devuelve true, el usuario ya existe
        if(existe){
            return false
        }
        // insertar el usuario si no existe
        val values = ContentValues().apply {
            put("usuario", usuario)
            put("contrasena", contrasena)
        }
        val resultado = db.insert("usuario", null, values)
        return resultado != -1L
    }

    fun validarIngreso(usuario: String, contrasena: String): Boolean {
        val db = readableDatabase
        // Buscar en la base de datos un usuario con los datos entregados
        val cursor = db.rawQuery("SELECT * FROM usuario WHERE usuario = ? AND contrasena = ?", arrayOf(usuario, contrasena))
        val existe = cursor.count > 0 //Hay elementos ? true : false
        cursor.close()
        return existe
    }

    // === Tipo de Limpieza CRUD ===
    fun obtenerTipoLimp(): List<TipoLimpieza> {
        val listaTipoLimp = mutableListOf<TipoLimpieza>() //Lista de tipos de limpieza
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM tipo_limpieza", null) //Obtener todos los registros
        // Recorrer el cursor para agregar los valores a la lista
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id_tipoLimp"))
                val nombreTipo = cursor.getString(cursor.getColumnIndexOrThrow("nombre_tipo"))

                val tipoLimpObj = TipoLimpieza(id, nombreTipo)
                listaTipoLimp.add(tipoLimpObj)
            } while (cursor.moveToNext())
        }
        cursor.close()
        // Regresar lista con todos los registros
        return listaTipoLimp
    }

    fun buscarTipoLimp(id: Int): TipoLimpieza? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM tipo_limpieza WHERE id_tipoLimp = ?", arrayOf(id.toString()))
        //crear variable de retorno
        var tipoLimpieza: TipoLimpieza? = null
        //si existe el registro guardar los valores en la variable
        if(cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id_tipoLimp"))
            val nombreTipo = cursor.getString(cursor.getColumnIndexOrThrow("nombre_tipo"))

            tipoLimpieza = TipoLimpieza(id, nombreTipo)
        }
        cursor.close()
        return tipoLimpieza
    }

    fun crearTipoLimp(nombreTipo: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nombre_tipo", nombreTipo)
        }
        val resultado = db.insert("tipo_limpieza", null, values)
        return resultado != -1L
    }

    fun actualizarTipoLimp(id: Int, nombreTipo: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nombre_tipo", nombreTipo)
        }

        val resultado = db.update("tipo_limpieza", values, "id_tipoLimp = ?", arrayOf(id.toString()))
        return resultado > 0
    }

    fun eliminarTipoLimp(id: Int): Boolean {
        val db = writableDatabase
        val resultado = db.delete("tipo_limpieza", "id_tipoLimp = ?", arrayOf(id.toString()))
        return resultado > 0
    }

    // === Tipo de Documento CRUD ===
    fun obtenerTipoDocu(): List<TipoDocumento> {
        val listaTipoDocu = mutableListOf<TipoDocumento>() //Lista de tipos de documento
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM tipo_documento", null) //Obtener todos los registros
        // Recorrer el cursor para agregar los valores a la lista
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id_tipoDocu"))
                val nombreTipo = cursor.getString(cursor.getColumnIndexOrThrow("nombre_tipo"))

                val tipoDocuObj = TipoDocumento(id, nombreTipo)
                listaTipoDocu.add(tipoDocuObj)
            } while (cursor.moveToNext())
        }
        cursor.close()
        // Regresar lista con todos los registros
        return listaTipoDocu
    }

    fun buscarTipoDocu(id: Int): TipoDocumento? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM tipo_documento WHERE id_tipoDocu = ?", arrayOf(id.toString()))
        //crear variable de retorno
        var tipoDocumento: TipoDocumento? = null
        //si existe el registro guardar los valores en la variable
        if(cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id_tipoDocu"))
            val nombreTipo = cursor.getString(cursor.getColumnIndexOrThrow("nombre_tipo"))

            tipoDocumento = TipoDocumento(id, nombreTipo)
        }
        cursor.close()
        return tipoDocumento
    }

    fun crearTipoDocu(nombreTipo: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nombre_tipo", nombreTipo)
        }
        val resultado = db.insert("tipo_documento", null, values)
        return resultado != -1L
    }

    fun actualizarTipoDocu(id: Int, nombreTipo: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nombre_tipo", nombreTipo)
        }

        val resultado = db.update("tipo_documento", values, "id_tipoDocu = ?", arrayOf(id.toString()))
        return resultado > 0
    }

    fun eliminarTipoDocu(id: Int): Boolean {
        val db = writableDatabase
        val resultado = db.delete("tipo_documento", "id_tipoDocu = ?", arrayOf(id.toString()))
        return resultado > 0
    }

    // === Cliente CRUD ===
    fun obtenerCliente(): List<Cliente> {
        val listaCliente = mutableListOf<Cliente>() //Lista de clientes
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM cliente", null) //Obtener todos los registros
        // Recorrer el cursor para agregar los valores a la lista
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id_cliente"))
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre_cliente"))
                val apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido_cliente"))
                val direccion = cursor.getString(cursor.getColumnIndexOrThrow("direccion_cliente"))
                val telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono_cliente"))
                val correo = cursor.getString(cursor.getColumnIndexOrThrow("correo_cliente"))
                val observacion = cursor.getString(cursor.getColumnIndexOrThrow("observacion_cliente"))

                val clienteObj = Cliente(id, nombre, apellido, direccion, telefono, correo, observacion)
                listaCliente.add(clienteObj)
            } while (cursor.moveToNext())
        }
        cursor.close()
        // Regresar lista con todos los registros
        return listaCliente
    }

    fun buscarCliente(id: Int): Cliente? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM cliente WHERE id_cliente = ?", arrayOf(id.toString()))
        //crear variable de retorno
        var cliente: Cliente? = null
        //si existe el registro guardar los valores en la variable
        if(cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id_cliente"))
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre_cliente"))
            val apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido_cliente"))
            val direccion = cursor.getString(cursor.getColumnIndexOrThrow("direccion_cliente"))
            val telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono_cliente"))
            val correo = cursor.getString(cursor.getColumnIndexOrThrow("correo_cliente"))
            val observacion = cursor.getString(cursor.getColumnIndexOrThrow("observacion_cliente"))

            cliente = Cliente(id, nombre, apellido, direccion, telefono, correo, observacion)
        }
        cursor.close()
        return cliente
    }

    fun crearCliente(nombre: String, apellido: String, direccion: String, telefono: String, correo: String, observacion: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nombre_cliente", nombre)
            put("apellido_cliente", apellido)
            put("direccion_cliente", direccion)
            put("telefono_cliente", telefono)
            put("correo_cliente", correo)
            put("observacion_cliente", observacion)
        }
        val resultado = db.insert("cliente", null, values)
        return resultado != -1L
    }

    fun actualizarCliente(id: Int, nombre: String, apellido: String, direccion: String, telefono: String, correo: String, observacion: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nombre_cliente", nombre)
            put("apellido_cliente", apellido)
            put("direccion_cliente", direccion)
            put("telefono_cliente", telefono)
            put("correo_cliente", correo)
            put("observacion_cliente", observacion)
        }

        val resultado = db.update("cliente", values, "id_cliente = ?", arrayOf(id.toString()))
        return resultado > 0
    }

    fun eliminarCliente(id: Int): Boolean {
        val db = writableDatabase
        val resultado = db.delete("cliente", "id_cliente = ?", arrayOf(id.toString()))
        return resultado > 0
    }

    // === Empleado CRUD ===
    fun obtenerEmpleado(): List<Empleado> {
        val listaEmpleado = mutableListOf<Empleado>() //Lista de empleados
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM empleado", null) //Obtener todos los registros
        // Recorrer el cursor para agregar los valores a la lista
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getString(cursor.getColumnIndexOrThrow("id_empleado"))
                val tipoDocu = cursor.getInt(cursor.getColumnIndexOrThrow("id_tipoDocu"))
                val documento = cursor.getString(cursor.getColumnIndexOrThrow("documento_empleado"))
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre_empleado"))
                val apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido_empleado"))
                val telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono_empleado"))
                val correo = cursor.getString(cursor.getColumnIndexOrThrow("correo_empleado"))

                val empleadoObj = Empleado(id, tipoDocu, documento, nombre, apellido, telefono, correo)
                listaEmpleado.add(empleadoObj)
            } while (cursor.moveToNext())
        }
        cursor.close()
        // Regresar lista con todos los registros
        return listaEmpleado
    }

    fun buscarEmpleado(id: String): Empleado? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM empleado WHERE id_empleado = ?", arrayOf(id))
        //crear variable de retorno
        var empleado: Empleado? = null
        //si existe el registro guardar los valores en la variable
        if(cursor.moveToFirst()) {
            val id = cursor.getString(cursor.getColumnIndexOrThrow("id_empleado"))
            val tipoDocu = cursor.getInt(cursor.getColumnIndexOrThrow("id_tipoDocu"))
            val documento = cursor.getString(cursor.getColumnIndexOrThrow("documento_empleado"))
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre_empleado"))
            val apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido_empleado"))
            val telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono_empleado"))
            val correo = cursor.getString(cursor.getColumnIndexOrThrow("correo_empleado"))

            empleado = Empleado(id, tipoDocu, documento, nombre, apellido, telefono, correo)
        }
        cursor.close()
        return empleado
    }

    fun crearEmpleado(tipoDocu: Int, documento: String, nombre: String, apellido: String, telefono: String, correo: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("id_tipoDocu", tipoDocu)
            put("documento_empleado", documento)
            put("nombre_empleado", nombre)
            put("apellido_empleado", apellido)
            put("telefono_empleado", telefono)
            put("correo_empleado", correo)
        }
        val resultado = db.insert("empleado", null, values)
        return resultado != -1L
    }

    fun actualizarEmpleado(id: String, tipoDocu: Int, documento: String, nombre: String, apellido: String, telefono: String, correo: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("id_tipoDocu", tipoDocu)
            put("documento_empleado", documento)
            put("nombre_empleado", nombre)
            put("apellido_empleado", apellido)
            put("telefono_empleado", telefono)
            put("correo_empleado", correo)
        }

        val resultado = db.update("empleado", values, "id_empleado = ?", arrayOf(id))
        return resultado > 0
    }

    fun eliminarEmpleado(id: String): Boolean {
        val db = writableDatabase
        val resultado = db.delete("empleado", "id_empleado = ?", arrayOf(id))
        return resultado > 0
    }

    // === Equipo CRUD ===
    fun obtenerEquipo(): List<Equipo> {
        val listaEquipo = mutableListOf<Equipo>() //Lista de equipos
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM equipo", null) //Obtener todos los registros
        // Recorrer el cursor para agregar los valores a la lista
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id_equipo"))
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre_equipo"))
                val lider = cursor.getString(cursor.getColumnIndexOrThrow("lider"))
                val miembro1 = cursor.getString(cursor.getColumnIndexOrThrow("miembro1"))
                val miembro2 = cursor.getString(cursor.getColumnIndexOrThrow("miembro2"))

                val equipoObj = Equipo(id, nombre, lider, miembro1, miembro2)
                listaEquipo.add(equipoObj)
            } while (cursor.moveToNext())
        }
        cursor.close()
        // Regresar lista con todos los registros
        return listaEquipo
    }

    fun buscarEquipo(id: Int): Equipo? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM equipo WHERE id_equipo = ?", arrayOf(id.toString()))
        //crear variable de retorno
        var equipo: Equipo? = null
        //si existe el registro guardar los valores en la variable
        if(cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id_equipo"))
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre_equipo"))
            val lider = cursor.getString(cursor.getColumnIndexOrThrow("lider"))
            val miembro1 = cursor.getString(cursor.getColumnIndexOrThrow("miembro1"))
            val miembro2 = cursor.getString(cursor.getColumnIndexOrThrow("miembro2"))

            equipo = Equipo(id, nombre, lider, miembro1, miembro2)
        }
        cursor.close()
        return equipo
    }

    fun crearEquipo(nombre: String, lider: String, miembro1: String, miembro2: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nombre_equipo", nombre)
            put("lider", lider)
            put("miembro1", miembro1)
            put("miembro2", miembro2)
        }
        val resultado = db.insert("equipo", null, values)
        return resultado != -1L
    }

    fun actualizarEquipo(id: Int, nombre: String, lider: String, miembro1: String, miembro2: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nombre_equipo", nombre)
            put("lider", lider)
            put("miembro1", miembro1)
            put("miembro2", miembro2)
        }

        val resultado = db.update("equipo", values, "id_equipo = ?", arrayOf(id.toString()))
        return resultado > 0
    }

    fun eliminarEquipo(id: Int): Boolean {
        val db = writableDatabase
        val resultado = db.delete("equipo", "id_equipo = ?", arrayOf(id.toString()))
        return resultado > 0
    }

    // === Servicio CRUD ===
    fun obtenerServicio(): List<Servicio> {
        val listaServicio = mutableListOf<Servicio>() //Lista de servicios
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM servicio", null) //Obtener todos los registros
        // Recorrer el cursor para agregar los valores a la lista
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id_equipo"))
                val cliente = cursor.getInt(cursor.getColumnIndexOrThrow("id_cliente"))
                val equipo = cursor.getInt(cursor.getColumnIndexOrThrow("id_equipo"))
                val tipoLimpieza = cursor.getInt(cursor.getColumnIndexOrThrow("id_tipoLimp"))
                val fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha"))
                val hora = cursor.getString(cursor.getColumnIndexOrThrow("hora"))
                val tiempo = cursor.getString(cursor.getColumnIndexOrThrow("tiempo"))
                val finalizacion = cursor.getString(cursor.getColumnIndexOrThrow("finalizacion"))
                val precio = cursor.getInt(cursor.getColumnIndexOrThrow("precio"))
                val observacion = cursor.getString(cursor.getColumnIndexOrThrow("observacion_servicio"))

                val servicioObj = Servicio(id, cliente, equipo, tipoLimpieza, fecha, hora, tiempo, finalizacion, precio, observacion)
                listaServicio.add(servicioObj)
            } while (cursor.moveToNext())
        }
        cursor.close()
        // Regresar lista con todos los registros
        return listaServicio
    }

    fun buscarServicio(id: Int): Servicio? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM servicio WHERE id_servicio = ?", arrayOf(id.toString()))
        //crear variable de retorno
        var servicio: Servicio? = null
        //si existe el registro guardar los valores en la variable
        if(cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id_equipo"))
            val cliente = cursor.getInt(cursor.getColumnIndexOrThrow("id_cliente"))
            val equipo = cursor.getInt(cursor.getColumnIndexOrThrow("id_equipo"))
            val tipoLimpieza = cursor.getInt(cursor.getColumnIndexOrThrow("id_tipoLimp"))
            val fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha"))
            val hora = cursor.getString(cursor.getColumnIndexOrThrow("hora"))
            val tiempo = cursor.getString(cursor.getColumnIndexOrThrow("tiempo"))
            val finalizacion = cursor.getString(cursor.getColumnIndexOrThrow("finalizacion"))
            val precio = cursor.getInt(cursor.getColumnIndexOrThrow("precio"))
            val observacion = cursor.getString(cursor.getColumnIndexOrThrow("observacion_servicio"))

            servicio = Servicio(id, cliente, equipo, tipoLimpieza, fecha, hora, tiempo, finalizacion, precio, observacion)
        }
        cursor.close()
        return servicio
    }

    fun crearServicio(cliente: Int, equipo: Int, tipoLimpieza: Int, fecha: String, hora: String, tiempo: String, finalizacion: String, precio: Int, observacion: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("id_cliente", cliente)
            put("id_equipo", equipo)
            put("id_tipoLimpieza", tipoLimpieza)
            put("fecha", fecha)
            put("hora", hora)
            put("tiempo", tiempo)
            put("finalizacion", finalizacion)
            put("precio", precio)
            put("observacion_servicio", observacion)
        }
        val resultado = db.insert("servicio", null, values)
        return resultado != -1L
    }

    fun actualizarServicio(id: Int, cliente: Int, equipo: Int, tipoLimpieza: Int, fecha: String, hora: String, tiempo: String, finalizacion: String, precio: Int, observacion: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("id_cliente", cliente)
            put("id_equipo", equipo)
            put("id_tipoLimpieza", tipoLimpieza)
            put("fecha", fecha)
            put("hora", hora)
            put("tiempo", tiempo)
            put("finalizacion", finalizacion)
            put("precio", precio)
            put("observacion_servicio", observacion)
        }

        val resultado = db.update("servicio", values, "id_servicio = ?", arrayOf(id.toString()))
        return resultado > 0
    }

    fun eliminarServicio(id: Int): Boolean {
        val db = writableDatabase
        val resultado = db.delete("servicio", "id_servicio = ?", arrayOf(id.toString()))
        return resultado > 0
    }
}