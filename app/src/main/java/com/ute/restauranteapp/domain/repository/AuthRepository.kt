package com.ute.restauranteapp.domain.repository
import com.ute.restauranteapp.data.local.TokenDataStore
import com.ute.restauranteapp.domain.model.LoggedUser

interface AuthRepository {
    suspend fun login(username: String, password: String): Result<LoggedUser>
    suspend fun register(
        username: String,
        email: String,
        password: String,
        password2: String,
    ): Result<LoggedUser>
    suspend fun logout(): Result<Unit>
    suspend fun getStoredUser(): TokenDataStore.UserSnapshot?
    suspend fun isLoggedIn(): Boolean
}