package com.agenda.amm.data.model

data class TipoLimpieza (
    val idTipoLimp: Int,
    val nombreTipo: String
) {
    override fun toString(): String {
        return nombreTipo
    }
}