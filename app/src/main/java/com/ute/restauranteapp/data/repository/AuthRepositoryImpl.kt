// data/repository/AuthRepositoryImpl.kt
package com.ute.restauranteapp.data.repository

import com.google.gson.Gson
import com.ute.restauranteapp.data.local.TokenDataStore
import com.ute.restauranteapp.data.remote.api.AuthApi
import com.ute.restauranteapp.data.remote.dto.*
import com.ute.restauranteapp.domain.model.LoggedUser
import com.ute.restauranteapp.domain.repository.AuthRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.get

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val api:            AuthApi,
    private val tokenDataStore: TokenDataStore,
) : AuthRepository {

    override suspend fun login(username: String, password: String): Result<LoggedUser> =
        runCatching {
            val response = api.login(LoginRequest(username, password))
            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string() ?: ""
                error(parseErrorMessage(errorBody, response.code()))
            }
            val body = response.body()!!
            tokenDataStore.saveTokens(body.access, body.refresh)
            tokenDataStore.saveUser(
                body.userId,
                body.clienteId,
                body.username,
                body.email,
                body.isStaff
            )
            LoggedUser(
                id = body.userId,
                clienteId = body.clienteId,
                username = body.username,
                email = body.email,
                isStaff = body.isStaff
            )
        }

    override suspend fun register(
        username: String,
        email: String,
        password: String,
        password2: String,
    ): Result<LoggedUser> = runCatching {
        val response = api.register(RegisterRequest(username, email, password, password2))
        if (!response.isSuccessful) {
            val errorBody = response.errorBody()?.string() ?: ""
            error(parseErrorMessage(errorBody, response.code()))
        }
        val body = response.body()!!
        tokenDataStore.saveTokens(body.access, body.refresh)
        tokenDataStore.saveUser(
            body.userId,
            body.clienteId,
            body.username,
            body.email,
            body.isStaff
        )
        LoggedUser(
            id = body.userId,
            clienteId = body.clienteId,
            username = body.username,
            email = body.email,
            isStaff = body.isStaff
        )
    }

    override suspend fun logout(): Result<Unit> = runCatching {
        val refresh = tokenDataStore.getRefreshToken()
        if (refresh != null) {
            runCatching { api.logout(LogoutRequest(refresh)) }
        }
        tokenDataStore.clearSession()
    }

    override suspend fun getStoredUser(): TokenDataStore.UserSnapshot? =
        tokenDataStore.userSnapshot.first()

    override suspend fun isLoggedIn(): Boolean =
        !tokenDataStore.getAccessToken().isNullOrBlank()

    private fun parseErrorMessage(body: String, code: Int): String {
        return try {
            val map = Gson()
                .fromJson(body, Map::class.java)
            map["detail"]?.toString()
                ?: map["non_field_errors"]?.toString()
                ?: map.values.firstOrNull()?.toString()
                ?: "Error $code"
        } catch (e: Exception) {
            "Error $code"
        }
    }
}