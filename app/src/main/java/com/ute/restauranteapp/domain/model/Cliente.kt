package com.ute.restauranteapp.domain.model

data class Cliente(
    val id: Int,
    val nombreCompleto: String,
    val telefono: String,
    val correo: String,
    val fechaRegistro: String,
)

data class ClientePayload(
    val nombreCompleto: String,
    val telefono: String,
    val correo: String,
)

data class ClienteFilters(
    val nombreCompleto: String? = null,
    val correo: String? = null,
    val fechaRegistroAfter: String? = null,
    val fechaRegistroBefore: String? = null,
    val search: String? = null,
    val ordering: String? = null,
)