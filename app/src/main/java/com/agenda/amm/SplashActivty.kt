package com.agenda.amm

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import com.agenda.amm.activities.InicioActivity
import com.agenda.amm.activities.LoginActivity

class SplashActivty : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

        // Esperar 2 segundos
        Handler(Looper.getMainLooper()).postDelayed({
            val prefs = getSharedPreferences("usuario_sesion", MODE_PRIVATE)
            val sesionActiva = prefs.getBoolean("sesionIniciada", false)

            if (sesionActiva) {
                // Ir al inicio activity
                startActivity(Intent(this, InicioActivity::class.java))
            } else {
                // ir al login activity
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish()
        }, 2000)
    }
}