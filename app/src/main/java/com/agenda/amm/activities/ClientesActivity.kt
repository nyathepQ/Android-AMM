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
import com.agenda.amm.data.model.Cliente

class ClientesActivity : ComponentActivity() {

    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.clientes_activity)
        // datos
        dbHelper = DBHelper(this)
        // elementos
        val btnRegresar = findViewById<Button>(R.id.btnRegresar)
        val idSpinner = findViewById<Spinner>(R.id.idSpinner)
        val nombreEditText = findViewById<EditText>(R.id.nombreEditText)
        val apellidoEditText = findViewById<EditText>(R.id.apellidoEditText)
        val direccionEditText = findViewById<EditText>(R.id.direccionEditText)
        val telefonoEditText = findViewById<EditText>(R.id.telefonoEditText)
        val correoEditText = findViewById<EditText>(R.id.correoEditText)
        val observacionEditText = findViewById<EditText>(R.id.observacionEditText)
        val btnCrear = findViewById<Button>(R.id.btnCrear)
        val btnModificar = findViewById<Button>(R.id.btnModificar)
        val btnMostrar = findViewById<Button>(R.id.btnMostrar)
        val btnEliminar = findViewById<Button>(R.id.btnEliminar)

        // cargar datos de spinner
        recargarSpinner()

        // botones
        btnRegresar.setOnClickListener {
            val intent = Intent(this, InicioActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnCrear.setOnClickListener {
            val nombre = nombreEditText.text.toString()
            val apellido = apellidoEditText.text.toString()
            val direccion = direccionEditText.text.toString()
            val telefono = telefonoEditText.text.toString()
            val correo = correoEditText.text.toString()
            val observacion = observacionEditText.text.toString()
            val resultado = dbHelper.crearCliente(nombre, apellido, direccion, telefono, correo, observacion)
            if(resultado) {
                Toast.makeText(this, "Cliente $nombre $apellido creado", Toast.LENGTH_SHORT).show()
                recargarSpinner()
                reiniciarForm()
            } else {
                Toast.makeText(this, "Error al crear el cliente", Toast.LENGTH_SHORT).show()
            }
        }

        btnModificar.setOnClickListener {
            val idSeleccionado = idSpinner.selectedItem as Cliente
            val nombre = nombreEditText.text.toString()
            val apellido = apellidoEditText.text.toString()
            val direccion = direccionEditText.text.toString()
            val telefono = telefonoEditText.text.toString()
            val correo = correoEditText.text.toString()
            val observacion = observacionEditText.text.toString()
            val resultado = dbHelper.actualizarCliente(idSeleccionado.idCliente, nombre, apellido, direccion, telefono, correo, observacion)
            if(resultado) {
                Toast.makeText(this, "Cliente ID: ${idSeleccionado.idCliente} modificado", Toast.LENGTH_SHORT).show()
                recargarSpinner()
                reiniciarForm()
            } else {
                Toast.makeText(this, "Error al modificar el cliente", Toast.LENGTH_SHORT).show()
            }
        }

        btnMostrar.setOnClickListener {
            val idSeleccionado = idSpinner.selectedItem as Cliente
            nombreEditText.setText(idSeleccionado.nombreCliente)
            apellidoEditText.setText(idSeleccionado.apellidoCliente)
            direccionEditText.setText(idSeleccionado.direccionCliente)
            telefonoEditText.setText(idSeleccionado.telefonoCliente)
            correoEditText.setText(idSeleccionado.correoCliente)
            observacionEditText.setText(idSeleccionado.observacionCliente)
        }

        btnEliminar.setOnClickListener {
            val idSeleccionado = idSpinner.selectedItem as Cliente
            val resultado = dbHelper.eliminarCliente(idSeleccionado.idCliente)
            if(resultado) {
                Toast.makeText(this, "Cliente ID: ${idSeleccionado.idCliente} eliminado", Toast.LENGTH_SHORT).show()
                recargarSpinner()
                reiniciarForm()
            } else {
                Toast.makeText(this, "Error al eliminar el cliente", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun recargarSpinner() {
        val clientes = dbHelper.obtenerCliente()
        val idSpinner = findViewById<Spinner>(R.id.idSpinner)

        val adapter = object : ArrayAdapter<Cliente>(this, android.R.layout.simple_spinner_item, clientes) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                (view as TextView).text = clientes[position].idCliente.toString()
                return view
            }

            override fun getDropDownView(postion: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(postion, convertView, parent)
                (view as TextView).text = clientes[postion].toString()
                return view
            }
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        idSpinner.adapter = adapter
    }

    private fun reiniciarForm(){
        val nombreEditText = findViewById<EditText>(R.id.nombreEditText)
        val apellidoEditText = findViewById<EditText>(R.id.apellidoEditText)
        val direccionEditText = findViewById<EditText>(R.id.direccionEditText)
        val telefonoEditText = findViewById<EditText>(R.id.telefonoEditText)
        val correoEditText = findViewById<EditText>(R.id.correoEditText)
        val observacionEditText = findViewById<EditText>(R.id.observacionEditText)

        nombreEditText.setText("")
        apellidoEditText.setText("")
        direccionEditText.setText("")
        telefonoEditText.setText("")
        correoEditText.setText("")
        observacionEditText.setText("")
    }
}