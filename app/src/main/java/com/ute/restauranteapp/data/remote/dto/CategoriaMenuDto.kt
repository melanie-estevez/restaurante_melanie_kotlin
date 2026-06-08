package com.ute.restauranteapp.data.remote.dto

import com.ute.restauranteapp.domain.model.CategoriaMenu
import com.ute.restauranteapp.domain.model.CategoriaMenuPayload

data class CategoriaMenuDto(
    val id: Int,
    val nombre: String,
    val descripcion: String,
)

data class CategoriaMenuRequestDto(
    val nombre: String,
    val descripcion: String,
)

fun CategoriaMenuDto.toDomain() = CategoriaMenu(
    id = id,
    nombre = nombre,
    descripcion = descripcion,
)

fun CategoriaMenuPayload.toRequest() = CategoriaMenuRequestDto(
    nombre = nombre,
    descripcion = descripcion,
)