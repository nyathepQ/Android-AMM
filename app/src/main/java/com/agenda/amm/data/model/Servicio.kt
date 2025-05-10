package com.agenda.amm.data.model

data class Servicio (
    val id_servicio: Int,
    val id_cliente: Int,
    val id_equipo: Int,
    val id_tipoLimp: Int,
    val fecha: String,
    val hora: String,
    val tiempo: String,
    val finalizacion: String,
    val precio: Int,
    val observacion_servicio: String
)