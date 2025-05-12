package com.agenda.amm.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.net.toUri
import com.agenda.amm.R
import com.agenda.amm.data.db.DBHelper

class LoginActivity : ComponentActivity() {

    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        dbHelper = DBHelper(this)

        val usernameEditText = findViewById<EditText>(R.id.formUsername)
        val passwordEditText = findViewById<EditText>(R.id.formPass)
        val btnLogin = findViewById<Button>(R.id.btnIngresar)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)
        val facebookIcon = findViewById<ImageView>(R.id.facebook_url)
        val instagramIcon = findViewById<ImageView>(R.id.instagram_url)

        btnLogin.setOnClickListener {
            val usuario = usernameEditText.text.toString()
            val contrasena = passwordEditText.text.toString()
            try {
                if (dbHelper.validarIngreso(usuario, contrasena)) {
                    val prefs = getSharedPreferences("usuario_sesion", MODE_PRIVATE)
                    with(prefs.edit()) {
                        putString("usuario", usuario)
                        putBoolean("sesionIniciada", true)
                        apply()
                    }
                    Toast.makeText(this, "Login exitoso", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, InicioActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT)
                        .show()
                }
            } catch(e: Exception) {
                Toast.makeText(this, "Error ${e.message}", Toast.LENGTH_LONG).show()
            }
        }

        btnRegistrar.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        facebookIcon.setOnClickListener { abrirFacebook() }
        instagramIcon.setOnClickListener{ abrirInstagram() }
    }

    private fun abrirFacebook() {
        val uri = "fb://facewebmodal/f?href=https://www.facebook.com/AlfprofessionalC".toUri()
        val intent = Intent(Intent.ACTION_VIEW, uri)
        try {
            startActivity(intent)
        } catch (e: Exception) {
            startActivity(Intent(Intent.ACTION_VIEW, "https://www.facebook.com/AlfprofessionalC".toUri()))
        }
    }

    private  fun abrirInstagram() {
        val uri = "https://www.instagram.com/AlfprofessionalC".toUri()
        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            setPackage("com.instagram.android")
        }
        try {
            startActivity(intent)
        } catch (e: Exception) {
            startActivity(Intent(Intent.ACTION_VIEW, "https://www.instagram.com/AlfprofessionalC".toUri()))
        }
    }
}