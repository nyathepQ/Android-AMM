package com.agenda.amm.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.agenda.amm.R
import com.agenda.amm.data.db.DBHelper

class RegisterActivity : ComponentActivity() {

    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.register_activity)

        dbHelper = DBHelper(this)

        val usernameForm = findViewById<EditText>(R.id.formUsername)
        val passwordForm = findViewById<EditText>(R.id.formPass)
        val btnRegistro = findViewById<Button>(R.id.btnRegistrar)
        val btnRegresar = findViewById<Button>(R.id.btnRegresar)

        btnRegresar.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnRegistro.setOnClickListener {
            val usuario = usernameForm.text.toString()
            val contrasena = passwordForm.text.toString()

            if(dbHelper.registroUsuario(usuario, contrasena)) {
                Toast.makeText(this, "Usuario ${usuario} creado exitosamente", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Nombre de usuario ya existente", Toast.LENGTH_SHORT).show()
            }
        }
    }
}