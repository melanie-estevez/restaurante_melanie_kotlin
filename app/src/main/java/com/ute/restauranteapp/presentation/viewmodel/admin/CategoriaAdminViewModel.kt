package com.ute.restauranteapp.presentation.viewmodel.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ute.restauranteapp.domain.model.CategoriaMenu
import com.ute.restauranteapp.domain.model.CategoriaMenuPayload
import com.ute.restauranteapp.domain.repository.CategoriaMenuRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CategoriaAdminUiState(
    val isLoading: Boolean = false,
    val categorias: List<CategoriaMenu> = emptyList(),
    val selectedCategoria: CategoriaMenu? = null,
    val error: String? = null,
    val successMessage: String? = null
)

@HiltViewModel
class CategoriaAdminViewModel @Inject constructor(
    private val repository: CategoriaMenuRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CategoriaAdminUiState())
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
                            isLoading = false,
                            categorias = categorias
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Error al cargar categorías"
                        )
                    }
                }
        }
    }

    fun seleccionarCategoria(categoria: CategoriaMenu?) {
        _state.update { it.copy(selectedCategoria = categoria) }
    }

    fun guardarCategoria(
        id: Int?,
        nombre: String,
        descripcion: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val payload = CategoriaMenuPayload(
                nombre = nombre,
                descripcion = descripcion
            )

            val result = if (id == null) {
                repository.createCategoria(payload)
            } else {
                repository.updateCategoria(id, payload)
            }

            result
                .onSuccess {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            selectedCategoria = null,
                            successMessage = "Categoría guardada correctamente"
                        )
                    }
                    cargarCategorias()
                    onSuccess()
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Error al guardar categoría"
                        )
                    }
                }
        }
    }

    fun eliminarCategoria(id: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            repository.deleteCategoria(id)
                .onSuccess {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            successMessage = "Categoría eliminada correctamente"
                        )
                    }
                    cargarCategorias()
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Error al eliminar categoría"
                        )
                    }
                }
        }
    }
}