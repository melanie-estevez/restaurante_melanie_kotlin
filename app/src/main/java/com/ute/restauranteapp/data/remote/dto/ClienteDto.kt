package com.ute.restauranteapp.data.remote.dto
import com.google.gson.annotations.SerializedName
import com.ute.restauranteapp.domain.model.Cliente
import com.ute.restauranteapp.domain.model.ClientePayload

data class ClienteDto(
    val id: Int,

    @SerializedName("nombre_completo")
    val nombreCompleto: String,

    val telefono: String,

    val correo: String,

    @SerializedName("fecha_registro")
    val fechaRegistro: String,
)

data class ClienteRequestDto(
    @SerializedName("nombre_completo")
    val nombreCompleto: String,

    val telefono: String,

    val correo: String,
)

fun ClienteDto.toDomain() = Cliente(
    id = id,
    nombreCompleto = nombreCompleto,
    telefono = telefono,
    correo = correo,
    fechaRegistro = fechaRegistro,
)

fun ClientePayload.toRequest() = ClienteRequestDto(
    nombreCompleto = nombreCompleto,
    telefono = telefono,
    correo = correo,
)