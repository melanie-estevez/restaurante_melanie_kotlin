package com.ute.restauranteapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ute.restauranteapp.domain.model.Plato
import com.ute.restauranteapp.domain.repository.PlatoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class PlatoDetailUiState {

    object Loading : PlatoDetailUiState()

    data class Success(
        val plato: Plato
    ) : PlatoDetailUiState()

    data class Error(
        val message: String
    ) : PlatoDetailUiState()
}

@HiltViewModel
class PlatoDetailViewModel @Inject constructor(
    private val repository: PlatoRepository
) : ViewModel() {

    private val _state =
        MutableStateFlow<PlatoDetailUiState>(
            PlatoDetailUiState.Loading
        )

    val state: StateFlow<PlatoDetailUiState> =
        _state.asStateFlow()

    fun load(platoId: Int) {

        viewModelScope.launch {

            _state.value =
                PlatoDetailUiState.Loading

            repository.getPlato(platoId)
                .onSuccess { plato ->

                    _state.value =
                        PlatoDetailUiState.Success(plato)
                }
                .onFailure { error ->

                    _state.value =
                        PlatoDetailUiState.Error(
                            error.message ?: "Error cargando plato"
                        )
                }
        }
    }
}