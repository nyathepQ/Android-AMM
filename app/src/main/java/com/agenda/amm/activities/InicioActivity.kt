package com.agenda.amm.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.agenda.amm.MainActivity
import com.agenda.amm.R
import androidx.core.content.edit

class InicioActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.inicio_activity)

        val prefs = getSharedPreferences("usuario_sesion", MODE_PRIVATE)
        val username = prefs.getString("usuario", "Usuario")
        val textUser = "$username (Cerrar Sesión)"

        val btnCerrar = findViewById<Button>(R.id.btnCerrarSesion)
        btnCerrar.text = textUser
        val btnServicios = findViewById<Button>(R.id.btnServicios)
        val btnClientes = findViewById<Button>(R.id.btnClientes)
        val btnEmpleados = findViewById<Button>(R.id.btnEmpleados)
        val btnEquipos = findViewById<Button>(R.id.btnEquipos)
        val btnTipos = findViewById<Button>(R.id.btnTipos)

        btnCerrar.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Cerrar sesión")
                .setMessage("¿Esta seguro de que desea cerrar sesión")
                .setPositiveButton("Sí") { dialog, _ ->
                    //borrar sesión guardada
                    prefs.edit { clear() }

                    //volver al login
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }
}