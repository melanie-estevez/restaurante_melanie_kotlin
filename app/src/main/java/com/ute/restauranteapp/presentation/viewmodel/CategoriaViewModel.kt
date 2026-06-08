package com.ute.restauranteapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ute.restauranteapp.domain.model.CategoriaMenu
import com.ute.restauranteapp.domain.repository.CategoriaMenuRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CategoriaUiState(
    val categorias: List<CategoriaMenu> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class CategoriaViewModel @Inject constructor(
    private val repository: CategoriaMenuRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CategoriaUiState())
    val state = _state.asStateFlow()

    init {
        cargarCategorias()
    }

    fun cargarCategorias() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            repository.getCategorias()
                .onSuccess { categorias ->
                    _state.update {
                        it.copy(
                            categorias = categorias,
                            isLoading = false,
                            error = null
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Error cargando categorías"
                        )
                    }
                }
        }
    }
}