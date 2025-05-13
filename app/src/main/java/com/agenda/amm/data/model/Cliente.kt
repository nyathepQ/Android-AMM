package com.agenda.amm.data.model

data class Cliente (
    val idCliente: Int,
    val nombreCliente: String,
    val apellidoCliente: String,
    val direccionCliente: String,
    val telefonoCliente: String,
    val correoCliente: String,
    val observacionCliente: String?
) {
    override fun toString(): String {
        return "$nombreCliente $apellidoCliente"
    }
}