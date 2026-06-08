package com.ute.restauranteapp.data.remote.dto
import com.google.gson.annotations.SerializedName
import com.ute.restauranteapp.domain.model.Plato
import com.ute.restauranteapp.domain.model.PlatoPayload

data class PlatoDto(
    val id: Int,
    val categoria: Int,

    @SerializedName("categoria_nombre")
    val categoriaNombre: String,

    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val disponible: Boolean,
)

data class PlatoRequestDto(
    val categoria: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val disponible: Boolean,
)

fun PlatoDto.toDomain() = Plato(
    id = id,
    categoria = categoria,
    categoriaNombre = categoriaNombre,
    nombre = nombre,
    descripcion = descripcion,
    precio = precio,
    disponible = disponible,
)

fun PlatoPayload.toRequest() = PlatoRequestDto(
    categoria = categoria,
    nombre = nombre,
    descripcion = descripcion,
    precio = precio,
    disponible = disponible,
)