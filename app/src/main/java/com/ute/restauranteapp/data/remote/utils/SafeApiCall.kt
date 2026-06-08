package com.ute.restauranteapp.data.remote.utils

import retrofit2.Response

suspend fun <T> safeApiCall(
    call: suspend () -> Response<T>
): Result<T> {
    return try {

        val response = call()

        if (response.isSuccessful) {

            val body = response.body()

            if (body != null) {
                Result.success(body)
            } else {
                Result.failure(
                    Exception("Respuesta vacía del servidor")
                )
            }

        } else {

            val errorBody = response.errorBody()?.string()

            Result.failure(
                Exception(
                    "HTTP ${response.code()}\n$errorBody"
                )
            )
        }

    } catch (e: Exception) {
        Result.failure(e)
    }
}