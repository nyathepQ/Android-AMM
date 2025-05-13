package com.agenda.amm.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.agenda.amm.R
import com.agenda.amm.data.db.DBHelper
import com.agenda.amm.data.model.Empleado
import com.agenda.amm.data.model.TipoDocumento
import kotlin.random.Random

class EmpleadosActivity : ComponentActivity() {

    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.empleados_activity)
        //cargar datos
        dbHelper = DBHelper(this)
        //elementos
        val btnRegresar = findViewById<Button>(R.id.btnRegresar)
        val idSpinner = findViewById<Spinner>(R.id.idSpinner)
        val tipoDocuSpinner = findViewById<Spinner>(R.id.idTipoDocuSpinner)
        val documentoEditText = findViewById<EditText>(R.id.documentoEditText)
        val nombreEditText = findViewById<EditText>(R.id.nombreEditText)
        val apellidoEditText = findViewById<EditText>(R.id.apellidoEditText)
        val telefonoEditText = findViewById<EditText>(R.id.telefonoEditText)
        val correoEditText = findViewById<EditText>(R.id.correoEditText)
        val btnCrear = findViewById<Button>(R.id.btnCrear)
        val btnModificar = findViewById<Button>(R.id.btnModificar)
        val btnMostrar = findViewById<Button>(R.id.btnMostrar)
        val btnEliminar = findViewById<Button>(R.id.btnEliminar)

        //cargar datos de spinner
        recargarSpinner(0) // tipo documento
        recargarSpinner() // id empleados

        //botones
        btnRegresar.setOnClickListener {
            val intent = Intent(this, InicioActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnCrear.setOnClickListener {
            val tipoDocu = tipoDocuSpinner.selectedItem as TipoDocumento
            val documento = documentoEditText.text.toString()
            val nombre = nombreEditText.text.toString()
            val apellido = apellidoEditText.text.toString()
            val telefono = telefonoEditText.text.toString()
            val correo = correoEditText.text.toString()
            val id = generarIdEmpleados(nombre, apellido)
            val resultado = dbHelper.crearEmpleado(id, tipoDocu.idTipoDocu, documento, nombre, apellido, telefono, correo)
            if (resultado) {
                Toast.makeText(this, "Empleado $nombre $apellido creado", Toast.LENGTH_SHORT).show()
                recargarSpinner()
                reiniciarForm()
            } else {
                Toast.makeText(this, "Error al crear el empleado", Toast.LENGTH_SHORT).show()
            }
        }

        btnModificar.setOnClickListener {
            val idSeleccionado = idSpinner.selectedItem as Empleado
            val tipoDocu = tipoDocuSpinner.selectedItem as TipoDocumento
            val documento = documentoEditText.text.toString()
            val nombre = nombreEditText.text.toString()
            val apellido = apellidoEditText.text.toString()
            val telefono = telefonoEditText.text.toString()
            val correo = correoEditText.text.toString()
            val resultado = dbHelper.actualizarEmpleado(idSeleccionado.idEmpleado, tipoDocu.idTipoDocu, documento, nombre, apellido, telefono, correo)
            if(resultado) {
                Toast.makeText(this, "Empleado ID: ${idSeleccionado.idEmpleado} modificado", Toast.LENGTH_SHORT).show()
                recargarSpinner()
                reiniciarForm()
            } else {
                Toast.makeText(this, "Error al modificar el empleado", Toast.LENGTH_SHORT).show()
            }
        }

        btnMostrar.setOnClickListener {
            val idSeleccionado = idSpinner.selectedItem as Empleado
            tipoDocuSpinner.setSelection(idSeleccionado.idTipoDocu)
            documentoEditText.setText(idSeleccionado.documentoEmpleado)
            nombreEditText.setText(idSeleccionado.nombreEmpleado)
            apellidoEditText.setText(idSeleccionado.apellidoEmpleado)
            telefonoEditText.setText(idSeleccionado.telefonoEmpleado)
            correoEditText.setText(idSeleccionado.correoEmpleado)
        }

        btnEliminar.setOnClickListener {
            val idSeleccionado = idSpinner.selectedItem as Empleado
            val resultado = dbHelper.eliminarEmpleado(idSeleccionado.idEmpleado)
            if(resultado) {
                Toast.makeText(this, "Empleado ID: ${idSeleccionado.idEmpleado} eliminado", Toast.LENGTH_SHORT).show()
                recargarSpinner()
                reiniciarForm()
            } else {
                Toast.makeText(this, "Error al eliminar el empleado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun recargarSpinner(tipo: Int = 1) {
        if (tipo == 0) {
            val tiposDocu = dbHelper.obtenerTipoDocu()
            val tipoDocuSpinner = findViewById<Spinner>(R.id.idTipoDocuSpinner)
            val adapter = object : ArrayAdapter<TipoDocumento>(this, android.R.layout.simple_spinner_item, tiposDocu) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getView(position, convertView, parent)
                    (view as TextView).text = tiposDocu[position].toString()
                    return view
                }

                override fun getDropDownView(postion: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getDropDownView(postion, convertView, parent)
                    (view as TextView).text = tiposDocu[postion].toString()
                    return view
                }
            }
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            tipoDocuSpinner.adapter = adapter

        } else if (tipo == 1) {
            val empleados = dbHelper.obtenerEmpleado().filter { it.idEmpleado == "NA" }
            val idSpinner = findViewById<Spinner>(R.id.idSpinner)

            val adapter = object : ArrayAdapter<Empleado>(this, android.R.layout.simple_spinner_item, empleados) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getView(position, convertView, parent)
                    (view as TextView).text = empleados[position].idEmpleado
                    return view
                }

                override fun getDropDownView(postion: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getDropDownView(postion, convertView, parent)
                    (view as TextView).text = empleados[postion].toString()
                    return view
                }
            }
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            idSpinner.adapter = adapter
        }
    }

    private fun reiniciarForm() {
        val tipoDocuSpinner = findViewById<Spinner>(R.id.idTipoDocuSpinner)
        val documentoEditText = findViewById<EditText>(R.id.documentoEditText)
        val nombreEditText = findViewById<EditText>(R.id.nombreEditText)
        val apellidoEditText = findViewById<EditText>(R.id.apellidoEditText)
        val telefonoEditText = findViewById<EditText>(R.id.telefonoEditText)
        val correoEditText = findViewById<EditText>(R.id.correoEditText)

        tipoDocuSpinner.setSelection(0)
        documentoEditText.setText("")
        nombreEditText.setText("")
        apellidoEditText.setText("")
        telefonoEditText.setText("")
        correoEditText.setText("")
    }

    private fun generarIdEmpleados(nombre: String, apellido: String): String {
        //Primeros tres caracteres de nombre y apellido
        val nombreChars = nombre.padEnd(3, 'X').take(3).uppercase()
        val apellidoChars = apellido.padEnd(3, 'X').take(3).uppercase()
        // Generar 4 números aleatorios entre 0 y 9
        val numerosAleatorios = (1..4).map { Random.nextInt(0,10) }.joinToString("")
        return "EMP-$nombreChars$apellidoChars$numerosAleatorios"
    }
}