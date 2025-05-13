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
import com.agenda.amm.data.model.Equipo
import com.agenda.amm.data.model.Servicio
import com.agenda.amm.data.model.TipoLimpieza
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class ServiciosActivity : ComponentActivity() {

    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.servicios_activity)
        //datos
        dbHelper = DBHelper(this)
        //elementos
        val btnRegresar = findViewById<Button>(R.id.btnRegresar)
        val idSpinner = findViewById<Spinner>(R.id.idSpinner)
        val clienteSpinner = findViewById<Spinner>(R.id.clienteSpinner)
        val equipoSpinner = findViewById<Spinner>(R.id.equipoSpinner)
        val tipoLimpSpinner = findViewById<Spinner>(R.id.tipoLimpSpinner)
        val fechaEditText = findViewById<EditText>(R.id.fechaEditText)
        val horaEditText = findViewById<EditText>(R.id.horaEditText)
        val estimadoEditText = findViewById<EditText>(R.id.estimadoEditText)
        val finalizacionEditText = findViewById<EditText>(R.id.finalizacionEditText)
        val precioEditText = findViewById<EditText>(R.id.precioEditText)
        val observacionEditText = findViewById<EditText>(R.id.observacionEditText)
        val btnCrear = findViewById<Button>(R.id.btnCrear)
        val btnModificar = findViewById<Button>(R.id.btnModificar)
        val btnMostrar = findViewById<Button>(R.id.btnMostrar)
        val btnEliminar = findViewById<Button>(R.id.btnEliminar)

        //cargar spinners
        recargarSpinner()
        recargarSpinner(0)

        //botones
        btnRegresar.setOnClickListener {
            val intent = Intent(this, InicioActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnCrear.setOnClickListener {
            val cliente = clienteSpinner.selectedItem as Cliente
            val equipo = equipoSpinner.selectedItem as Equipo
            val tipoLimp = tipoLimpSpinner.selectedItem as TipoLimpieza
            val fecha = fechaEditText.text.toString()
            if(!esFechaValida(fecha)) {
                fechaEditText.error = "Fecha inválida"
                return@setOnClickListener
            }
            val hora = horaEditText.text.toString()
            if(!esHoraValida(hora)) {
                horaEditText.error = "Hora inválida"
                return@setOnClickListener
            }
            val estimado = estimadoEditText.text.toString()
            if(!esHoraValida(estimado)) {
                estimadoEditText.error = "Hora inválida"
                return@setOnClickListener
            }
            val finalizacion = sumHoras(hora, estimado)
            val precio = precioEditText.text.toString().toInt()
            val observacion = observacionEditText.text.toString()
            val resultado = dbHelper.crearServicio(cliente.idCliente, equipo.idEquipo, tipoLimp.idTipoLimp, fecha, hora, estimado, finalizacion, precio, observacion)
            if(resultado) {
                Toast.makeText(this, "Servicio del $fecha creado", Toast.LENGTH_SHORT).show()
                recargarSpinner()
                reiniciarForm(idSpinner, clienteSpinner, equipoSpinner, tipoLimpSpinner, fechaEditText, horaEditText, estimadoEditText, finalizacionEditText, precioEditText, observacionEditText)
            } else {
                Toast.makeText(this, "Error al crear el servicio", Toast.LENGTH_SHORT).show()
            }
        }

        btnModificar.setOnClickListener {
            val idSeleccionado = idSpinner.selectedItem as Servicio
            val cliente = clienteSpinner.selectedItem as Cliente
            val equipo = equipoSpinner.selectedItem as Equipo
            val tipoLimp = tipoLimpSpinner.selectedItem as TipoLimpieza
            val fecha = fechaEditText.text.toString()
            if(!esFechaValida(fecha)) {
                fechaEditText.error = "Fecha inválida"
                return@setOnClickListener
            }
            val hora = horaEditText.text.toString()
            if(!esHoraValida(hora)) {
                horaEditText.error = "Hora inválida"
                return@setOnClickListener
            }
            val estimado = estimadoEditText.text.toString()
            if(!esHoraValida(estimado)) {
                estimadoEditText.error = "Hora inválida"
                return@setOnClickListener
            }
            val finalizacion = sumHoras(hora, estimado)
            val precio = precioEditText.text.toString().toInt()
            val observacion = observacionEditText.text.toString()
            val resultado = dbHelper.actualizarServicio(idSeleccionado.idServicio, cliente.idCliente, equipo.idEquipo, tipoLimp.idTipoLimp, fecha, hora, estimado, finalizacion, precio, observacion)
            if(resultado) {
                Toast.makeText(this, "Servicio ID: ${idSeleccionado.idServicio} modificado", Toast.LENGTH_SHORT).show()
                recargarSpinner()
                reiniciarForm(idSpinner, clienteSpinner, equipoSpinner, tipoLimpSpinner, fechaEditText, horaEditText, estimadoEditText, finalizacionEditText, precioEditText, observacionEditText)
            } else {
                Toast.makeText(this, "Error al modificar el servicio", Toast.LENGTH_SHORT).show()
            }
        }

        btnMostrar.setOnClickListener {
            val idSeleccionado = idSpinner.selectedItem as Servicio
            clienteSpinner.setSelection(idSeleccionado.idCliente)
            equipoSpinner.setSelection(idSeleccionado.idEquipo)
            tipoLimpSpinner.setSelection(idSeleccionado.idTipoLimp)
            fechaEditText.setText(idSeleccionado.fecha)
            horaEditText.setText(idSeleccionado.hora)
            estimadoEditText.setText(idSeleccionado.tiempo)
            finalizacionEditText.setText(idSeleccionado.finalizacion)
            precioEditText.setText(idSeleccionado.precio.toString())
            observacionEditText.setText(idSeleccionado.observacionServicio)
        }

        btnEliminar.setOnClickListener {
            val idSeleccionado = idSpinner.selectedItem as Servicio
            val resultado = dbHelper.eliminarServicio(idSeleccionado.idServicio)
            if(resultado) {
                Toast.makeText(this, "Servicio ID: ${idSeleccionado.idServicio} eliminado", Toast.LENGTH_SHORT).show()
                recargarSpinner()
                reiniciarForm(idSpinner, clienteSpinner, equipoSpinner, tipoLimpSpinner, fechaEditText, horaEditText, estimadoEditText, finalizacionEditText, precioEditText, observacionEditText)
            } else {
                Toast.makeText(this, "Error al eliminar el servicio", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun recargarSpinner(tipo: Int = 1) {
        if (tipo == 0) {
            val clientes = dbHelper.obtenerCliente()
            val equipos = dbHelper.obtenerEquipo()
            val tipoLimp = dbHelper.obtenerTipoLimp()
            val clienteSpinner = findViewById<Spinner>(R.id.clienteSpinner)
            val equipoSpinner = findViewById<Spinner>(R.id.equipoSpinner)
            val tipoLimpSpinner = findViewById<Spinner>(R.id.tipoLimpSpinner)

            val adapterCl = object : ArrayAdapter<Cliente>(this, android.R.layout.simple_spinner_item, clientes) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getView(position, convertView, parent)
                    (view as TextView).text = clientes[position].toString()
                    return view
                }
                override fun getDropDownView(postion: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getDropDownView(postion, convertView, parent)
                    (view as TextView).text = clientes[postion].toString()
                    return view
                }
            }
            val adapterEq = object : ArrayAdapter<Equipo>(this, android.R.layout.simple_spinner_item, equipos) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getView(position, convertView, parent)
                    (view as TextView).text = equipos[position].toString()
                    return view
                }
                override fun getDropDownView(postion: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getDropDownView(postion, convertView, parent)
                    (view as TextView).text = equipos[postion].toString()
                    return view
                }
            }
            val adapterTl = object : ArrayAdapter<TipoLimpieza>(this, android.R.layout.simple_spinner_item, tipoLimp) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getView(position, convertView, parent)
                    (view as TextView).text = tipoLimp[position].toString()
                    return view
                }
                override fun getDropDownView(postion: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getDropDownView(postion, convertView, parent)
                    (view as TextView).text = tipoLimp[postion].toString()
                    return view
                }
            }
            adapterCl.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            adapterEq.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            adapterTl.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            clienteSpinner.adapter = adapterCl
            equipoSpinner.adapter = adapterEq
            tipoLimpSpinner.adapter = adapterTl
        } else if (tipo == 1) {
            val servicios = dbHelper.obtenerServicio()
            val idSpinner = findViewById<Spinner>(R.id.idSpinner)

            val adapter = object : ArrayAdapter<Servicio>(this, android.R.layout.simple_spinner_item, servicios) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getView(position, convertView, parent)
                    (view as TextView).text = servicios[position].idEquipo.toString()
                    return view
                }
                override fun getDropDownView(postion: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getDropDownView(postion, convertView, parent)
                    (view as TextView).text = servicios[postion].toString()
                    return view
                }
            }
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            idSpinner.adapter = adapter
        }
    }

    private fun reiniciarForm(id: Spinner, cliente: Spinner, equipo: Spinner, tipoLimp: Spinner, fecha: EditText, hora: EditText, estimado: EditText, finalizacion: EditText, precio: EditText, observacion: EditText) {
        id.setSelection(0)
        cliente.setSelection(0)
        equipo.setSelection(0)
        tipoLimp.setSelection(0)
        fecha.setText("")
        hora.setText("")
        estimado.setText("")
        finalizacion.setText("")
        precio.setText("")
        observacion.setText("")
    }

    private fun esFechaValida(fecha: String): Boolean {
        if (!fecha.matches(Regex("\\d{4}/\\d{2}/\\d{2}"))) return false

        val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        sdf.isLenient = false
        return try {
            sdf.parse(fecha)
            true
        } catch (e: ParseException) {
            false
        }
    }

    private fun esHoraValida(hora: String): Boolean {
        if (!hora.matches(Regex("\\d{2}:\\d{2}"))) return false

        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        sdf.isLenient = false
        return try {
            sdf.parse(hora)
            true
            } catch (e: ParseException) {
            false
        }
    }

    private fun sumHoras(hora: String, estimado: String): String {
        //convertir a minutos
        fun horaAMinutos(hora: String): Int {
            val partes = hora.split(":")
            val horas = partes[0].toInt()
            val minutos = partes[1].toInt()
            return horas * 60 + minutos
        }
        //convertir a minutos a formato hh:mm
        fun minutosAHora(minutos: Int): String {
            val horas = (minutos / 60) %24 //formato 24 horas
            val minutosRestantes = minutos % 60
            return String.format(Locale.US, "%02d:%02d", horas, minutosRestantes)
        }

        val totalMinutos = horaAMinutos(hora) + horaAMinutos(estimado)
        return minutosAHora(totalMinutos)
    }
}