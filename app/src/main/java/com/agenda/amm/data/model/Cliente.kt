package com.agenda.amm.data.model

data class Cliente (
    val id_cliente: Int,
    val nombre_cliente: String,
    val apellido_cliente: String,
    val direccion_cliente: String,
    val telefono_cliente: String,
    val correo_cliente: String,
    val observacion_cliente: String
)