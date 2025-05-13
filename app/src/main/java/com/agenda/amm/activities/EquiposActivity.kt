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
import com.agenda.amm.data.model.Equipo

class EquiposActivity : ComponentActivity() {

    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.equipos_activity)
        //datos
        dbHelper = DBHelper(this)
        //elementos
        val btnRegresar = findViewById<Button>(R.id.btnRegresar)
        val idSpinner = findViewById<Spinner>(R.id.idSpinner)
        val nombreEditText = findViewById<EditText>(R.id.nombreEditText)
        val liderSpinner = findViewById<Spinner>(R.id.liderSpinner)
        val miembro1Spinner = findViewById<Spinner>(R.id.miembro1Spinner)
        val miembro2Spinner = findViewById<Spinner>(R.id.miembro2Spinner)
        val btnCrear = findViewById<Button>(R.id.btnCrear)
        val btnModificar = findViewById<Button>(R.id.btnModificar)
        val btnMostrar = findViewById<Button>(R.id.btnMostrar)
        val btnEliminar = findViewById<Button>(R.id.btnEliminar)

        //cargar datos
        recargarSpinner()
        recargarSpinner(0)

        //botones
        btnRegresar.setOnClickListener {
            val intent = Intent(this, InicioActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnCrear.setOnClickListener {
            val nombre = nombreEditText.text.toString()
            val lider = liderSpinner.selectedItem as Empleado
            val miembro1 = miembro1Spinner.selectedItem as Empleado
            val miembro2 = miembro2Spinner.selectedItem as Empleado
            val resultado = dbHelper.crearEquipo(nombre, lider.idEmpleado, miembro1.idEmpleado, miembro2.idEmpleado)
            if(resultado) {
                Toast.makeText(this, "Equipo $nombre creado", Toast.LENGTH_SHORT).show()
                recargarSpinner()
                reiniciarForm(idSpinner, nombreEditText, liderSpinner, miembro1Spinner, miembro2Spinner)
            } else {
                Toast.makeText(this, "Error al crear el equipo", Toast.LENGTH_SHORT).show()
            }
        }

        btnModificar.setOnClickListener {
            val idSeleccionado = idSpinner.selectedItem as Equipo
            val nombre = nombreEditText.text.toString()
            val lider = liderSpinner.selectedItem as Empleado
            val miembro1 = miembro1Spinner.selectedItem as Empleado
            val miembro2 = miembro2Spinner.selectedItem as Empleado

            val resultado = dbHelper.actualizarEquipo(idSeleccionado.idEquipo, nombre, lider.idEmpleado, miembro1.idEmpleado, miembro2.idEmpleado)
            if(resultado) {
                Toast.makeText(this, "Equipo ID: ${idSeleccionado.idEquipo} modificado", Toast.LENGTH_SHORT).show()
                recargarSpinner()
                reiniciarForm(idSpinner, nombreEditText, liderSpinner, miembro1Spinner, miembro2Spinner)
            } else {
                Toast.makeText(this, "Error al modificar el equipo", Toast.LENGTH_SHORT).show()
            }
        }

        btnMostrar.setOnClickListener {
            val empleados = dbHelper.obtenerEmpleado()
            val idSeleccionado = idSpinner.selectedItem as Equipo
            nombreEditText.setText(idSeleccionado.nombreEquipo)
            //buscar posiciones
            val indexLider = empleados.indexOfFirst { it.idEmpleado == idSeleccionado.lider }
            val indexMiembro1 = empleados.indexOfFirst { it.idEmpleado == idSeleccionado.miembro1 }
            val indexMiembro2 = empleados.indexOfFirst { it.idEmpleado == idSeleccionado.miembro2 }

            liderSpinner.setSelection(indexLider)
            miembro1Spinner.setSelection(indexMiembro1)
            miembro2Spinner.setSelection(indexMiembro2)
        }

        btnEliminar.setOnClickListener {
            val idSeleccionado = idSpinner.selectedItem as Equipo
            val resultado = dbHelper.eliminarEquipo(idSeleccionado.idEquipo)
            if(resultado) {
                Toast.makeText(this, "Equipo ID: ${idSeleccionado.idEquipo} eliminado", Toast.LENGTH_SHORT).show()
                recargarSpinner()
                reiniciarForm(idSpinner, nombreEditText, liderSpinner, miembro1Spinner, miembro2Spinner)
            } else {
                Toast.makeText(this, "Error al eliminar el equipo", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun recargarSpinner(tipo: Int = 1) {
        if (tipo == 0) {
            val empleados = dbHelper.obtenerEmpleado()
            val liderSpinner = findViewById<Spinner>(R.id.liderSpinner)
            val miembro1Spinner = findViewById<Spinner>(R.id.miembro1Spinner)
            val miembro2Spinner = findViewById<Spinner>(R.id.miembro2Spinner)

            val adapterEmp = object : ArrayAdapter<Empleado>(this, android.R.layout.simple_spinner_item, empleados) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getView(position, convertView, parent)
                    (view as TextView).text = empleados[position].toString()
                    return view
                }
                override fun getDropDownView(postion: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getDropDownView(postion, convertView, parent)
                    (view as TextView).text = empleados[postion].toString()
                    return view
                }
            }
            adapterEmp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            liderSpinner.adapter = adapterEmp
            miembro1Spinner.adapter = adapterEmp
            miembro2Spinner.adapter = adapterEmp
        } else if (tipo == 1) {
            val equipos = dbHelper.obtenerEquipo()
            val idSpinner = findViewById<Spinner>(R.id.idSpinner)

            val adapter = object : ArrayAdapter<Equipo>(this, android.R.layout.simple_spinner_item, equipos) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getView(position, convertView, parent)
                    (view as TextView).text = equipos[position].idEquipo.toString()
                    return view
                }
                override fun getDropDownView(postion: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getDropDownView(postion, convertView, parent)
                    (view as TextView).text = equipos[postion].toString()
                    return view
                }
            }
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            idSpinner.adapter = adapter
        }
    }

    private fun reiniciarForm(id: Spinner, nombre: EditText, lider: Spinner, miembro1: Spinner, miembro2: Spinner) {
        id.setSelection(0)
        nombre.setText("")
        lider.setSelection(0)
        miembro1.setSelection(0)
        miembro2.setSelection(0)
    }
}