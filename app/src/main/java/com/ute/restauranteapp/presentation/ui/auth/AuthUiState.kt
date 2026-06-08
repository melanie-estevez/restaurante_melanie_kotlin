package com.ute.restauranteapp.presentation.ui.auth

import com.ute.restauranteapp.domain.model.LoggedUser

sealed interface AuthUiState {
    data object Idle        : AuthUiState
    data object Loading     : AuthUiState
    data class  Success(val user: LoggedUser) : AuthUiState
    data class  Error(val message: String)    : AuthUiState
}