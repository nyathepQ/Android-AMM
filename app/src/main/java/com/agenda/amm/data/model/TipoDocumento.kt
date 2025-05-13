package com.agenda.amm.data.model

data class TipoDocumento (
    val idTipoDocu: Int,
    val nombreTipo: String
) {
    override fun toString(): String {
        return nombreTipo
    }
}