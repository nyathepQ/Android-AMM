package com.agenda.amm.data.model

data class Servicio (
    val idServicio: Int,
    val idCliente: Int,
    val idEquipo: Int,
    val idTipoLimp: Int,
    val fecha: String,
    val hora: String,
    val tiempo: String,
    val finalizacion: String,
    val precio: Int,
    val observacionServicio: String?
)