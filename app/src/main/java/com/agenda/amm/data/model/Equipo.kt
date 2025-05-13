package com.agenda.amm.data.model

data class Equipo (
    val idEquipo: Int,
    val nombreEquipo: String,
    val lider: String,
    val miembro1: String,
    val miembro2: String
) {
    override fun toString(): String {
        return nombreEquipo
    }
}