package com.ute.restauranteapp.domain.model
data class AuthTokens(
    val access: String,
    val refresh: String,
)

data class LoggedUser(
    val id: Int,
    val clienteId: Int?,
    val username: String,
    val email: String,
    val isStaff: Boolean,
)