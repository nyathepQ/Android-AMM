package com.agenda.amm.data.model

data class Empleado (
    val idEmpleado: String,
    val idTipoDocu: Int,
    val documentoEmpleado: String,
    val nombreEmpleado: String,
    val apellidoEmpleado: String,
    val telefonoEmpleado: String,
    val correoEmpleado: String
) {
    override fun toString(): String {
        return "$nombreEmpleado $apellidoEmpleado"
    }
}