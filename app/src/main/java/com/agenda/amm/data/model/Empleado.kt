package com.agenda.amm.data.model

data class Empleado (
    val id_empleado: String,
    val id_tipoDocu: Int,
    val documento_empleado: String,
    val nombre_empleado: String,
    val apellido_empleado: String,
    val telefono_empleado: String,
    val correo_empleado: String
)