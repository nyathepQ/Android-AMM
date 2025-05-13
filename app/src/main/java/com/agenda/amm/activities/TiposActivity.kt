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
import com.agenda.amm.data.model.TipoDocumento
import com.agenda.amm.data.model.TipoLimpieza

class TiposActivity : ComponentActivity() {

    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tipos_activity)
        // datos
        dbHelper = DBHelper(this)
        // elementos
        val btnRegresar = findViewById<Button>(R.id.btnRegresar)
        val spinnerLimp = findViewById<Spinner>(R.id.idSpinnerLimp)
        val spinnerDocu = findViewById<Spinner>(R.id.idSpinnerDocu)
        val editTextNombreLimp = findViewById<EditText>(R.id.nombreEditTextLimp)
        val btnCrearLimp = findViewById<Button>(R.id.btnCrearLimp)
        val btnModificarLimp = findViewById<Button>(R.id.btnModificarLimp)
        val btnMostrarLimp = findViewById<Button>(R.id.btnMostrarLimp)
        val btnEliminarLimp = findViewById<Button>(R.id.btnEliminarLimp)
        val editTextNombreDocu = findViewById<EditText>(R.id.nombreEditTextDocu)
        val btnCrearDocu = findViewById<Button>(R.id.btnCrearDocu)
        val btnModificarDocu = findViewById<Button>(R.id.btnModificarDocu)
        val btnMostrarDocu = findViewById<Button>(R.id.btnMostrarDocu)
        val btnEliminarDocu = findViewById<Button>(R.id.btnEliminarDocu)

        //agregar datos al spinner
        recargarSpinners("Limpieza")
        recargarSpinners("Documento")


        btnRegresar.setOnClickListener {
            val intent = Intent(this, InicioActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnCrearLimp.setOnClickListener {
            val nombreLimp = editTextNombreLimp.text.toString()
            val resultado = dbHelper.crearTipoLimp(nombreLimp)
            if(resultado) {
                Toast.makeText(this, "Tipo de Limpieza $nombreLimp creado", Toast.LENGTH_SHORT).show()
                recargarSpinners("Limpieza")
                reiniciarForm("Limpieza")
            } else {
                Toast.makeText(this, "Error al crear el tipo de limpieza", Toast.LENGTH_SHORT).show()
            }
        }

        btnModificarLimp.setOnClickListener {
            val idSeleccionado = spinnerLimp.selectedItem as TipoLimpieza
            val nombreLimp = editTextNombreLimp.text.toString()
            val resultado = dbHelper.actualizarTipoLimp(idSeleccionado.idTipoLimp, nombreLimp)
            if(resultado) {
                Toast.makeText(this, "Tipo de Limpieza ID: ${idSeleccionado.idTipoLimp} modificado", Toast.LENGTH_SHORT).show()
                recargarSpinners("Limpieza")
                reiniciarForm("Limpieza")
            } else {
                Toast.makeText(this, "Error al modificar el tipo de limpieza", Toast.LENGTH_SHORT).show()
            }
        }

        btnMostrarLimp.setOnClickListener {
            val idSeleccionado = spinnerLimp.selectedItem.toString()
            editTextNombreLimp.setText(idSeleccionado)
        }

        btnEliminarLimp.setOnClickListener {
            val idSeleccionado = spinnerLimp.selectedItem as TipoLimpieza
            val resultado = dbHelper.eliminarTipoLimp(idSeleccionado.idTipoLimp)
            if(resultado) {
                Toast.makeText(this, "Tipo de Limpieza ID: ${idSeleccionado.idTipoLimp} eliminado", Toast.LENGTH_SHORT).show()
                recargarSpinners("Limpieza")
                reiniciarForm("Limpieza")
            } else {
                Toast.makeText(this, "Error al eliminar el tipo de limpieza", Toast.LENGTH_SHORT).show()
            }
        }

        btnCrearDocu.setOnClickListener {
            val nombreDocu = editTextNombreDocu.text.toString()
            val resultado = dbHelper.crearTipoDocu(nombreDocu)
            if(resultado) {
                Toast.makeText(this, "Tipo de Documento $nombreDocu creado", Toast.LENGTH_SHORT).show()
                recargarSpinners("Documento")
                reiniciarForm("Documento")
            } else {
                Toast.makeText(this, "Error al crear el tipo de documento", Toast.LENGTH_SHORT).show()
            }
        }

        btnModificarDocu.setOnClickListener {
            val idSeleccionado = spinnerDocu.selectedItem as TipoDocumento
            val nombreDocu = editTextNombreDocu.text.toString()
            val resultado = dbHelper.actualizarTipoDocu(idSeleccionado.idTipoDocu, nombreDocu)
            if(resultado) {
                Toast.makeText(this, "Tipo de Documento ID: ${idSeleccionado.idTipoDocu} modificado", Toast.LENGTH_SHORT).show()
                recargarSpinners("Documento")
                reiniciarForm("Documento")
            } else {
                Toast.makeText(this, "Error al modificar el tipo de documento", Toast.LENGTH_SHORT).show()
            }
        }

        btnMostrarDocu.setOnClickListener {
            val idSeleccionado = spinnerDocu.selectedItem.toString()
            editTextNombreDocu.setText(idSeleccionado)
        }

        btnEliminarDocu.setOnClickListener {
            val idSeleccionado = spinnerDocu.selectedItem as TipoDocumento
            val resultado = dbHelper.eliminarTipoDocu(idSeleccionado.idTipoDocu)
            if(resultado) {
                Toast.makeText(this, "Tipo de Documento ID: ${idSeleccionado.idTipoDocu} eliminado", Toast.LENGTH_SHORT).show()
                recargarSpinners("Documento")
                reiniciarForm("Documento")
            } else {
                Toast.makeText(this, "Error al eliminar el tipo de documento", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun recargarSpinners(tipo: String) {
        if(tipo == "Limpieza") {
            val tiposLimp = dbHelper.obtenerTipoLimp()
            val spinnerLimp = findViewById<Spinner>(R.id.idSpinnerLimp)

            val adapterLimp = object : ArrayAdapter<TipoLimpieza>(this, android.R.layout.simple_spinner_item, tiposLimp) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getView(position, convertView, parent)
                    (view as TextView).text = tiposLimp[position].idTipoLimp.toString()
                    return view
                }

                override fun getDropDownView(postion: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getDropDownView(postion, convertView, parent)
                    (view as TextView).text = tiposLimp[postion].toString()
                    return view
                }
            }
            adapterLimp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerLimp.adapter = adapterLimp
        } else if (tipo == "Documento") {
            val tiposDocu = dbHelper.obtenerTipoDocu()
            val spinnerDocu = findViewById<Spinner>(R.id.idSpinnerDocu)

            val adapterDocu = object : ArrayAdapter<TipoDocumento>(this, android.R.layout.simple_spinner_item, tiposDocu) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getView(position, convertView, parent)
                    (view as TextView).text = tiposDocu[position].idTipoDocu.toString()
                    return view
                }
                override fun getDropDownView(postion: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getDropDownView(postion, convertView, parent)
                    (view as TextView).text = tiposDocu[postion].toString()
                    return view
                }
            }
            adapterDocu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerDocu.adapter = adapterDocu
        }
    }

    private fun reiniciarForm(tipo: String){
        if(tipo == "Limpieza") {
            val editTextNombreLimp = findViewById<EditText>(R.id.nombreEditTextLimp)
            editTextNombreLimp.setText("")
        } else if (tipo == "Documento") {
            val editTextNombreDocu = findViewById<EditText>(R.id.nombreEditTextDocu)
            editTextNombreDocu.setText("")
        }
    }
}