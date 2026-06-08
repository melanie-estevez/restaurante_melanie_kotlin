package com.ute.restauranteapp.presentation.viewmodel.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ute.restauranteapp.domain.model.CategoriaMenu
import com.ute.restauranteapp.domain.model.Plato
import com.ute.restauranteapp.domain.model.PlatoPayload
import com.ute.restauranteapp.domain.repository.CategoriaMenuRepository
import com.ute.restauranteapp.domain.repository.PlatoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PlatoAdminUiState(
    val isLoading: Boolean = false,
    val platos: List<Plato> = emptyList(),
    val categorias: List<CategoriaMenu> = emptyList(),
    val selectedPlato: Plato? = null,
    val error: String? = null,
    val successMessage: String? = null
)

@HiltViewModel
class PlatoAdminViewModel @Inject constructor(
    private val platoRepository: PlatoRepository,
    private val categoriaRepository: CategoriaMenuRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PlatoAdminUiState())
    val state = _state.asStateFlow()

    init {
        cargarTodo()
    }

    fun cargarTodo() {
        cargarCategorias()
        cargarPlatos()
    }

    fun cargarPlatos() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            platoRepository.getPlatos()
                .onSuccess { platos ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            platos = platos
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Error al cargar platos"
                        )
                    }
                }
        }
    }

    fun cargarCategorias() {
        viewModelScope.launch {
            categoriaRepository.getCategorias()
                .onSuccess { categorias ->
                    _state.update {
                        it.copy(categorias = categorias)
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(error = error.message ?: "Error al cargar categorías")
                    }
                }
        }
    }

    fun seleccionarPlato(plato: Plato?) {
        _state.update { it.copy(selectedPlato = plato) }
    }

    fun guardarPlato(
        id: Int?,
        categoria: Int,
        nombre: String,
        descripcion: String,
        precio: Double,
        disponible: Boolean,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val payload = PlatoPayload(
                categoria = categoria,
                nombre = nombre,
                descripcion = descripcion,
                precio = precio,
                disponible = disponible
            )

            val result = if (id == null) {
                platoRepository.createPlato(payload)
            } else {
                platoRepository.updatePlato(id, payload)
            }

            result
                .onSuccess {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            selectedPlato = null,
                            successMessage = "Plato guardado correctamente"
                        )
                    }
                    cargarPlatos()
                    onSuccess()
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Error al guardar plato"
                        )
                    }
                }
        }
    }

    fun eliminarPlato(id: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            platoRepository.deletePlato(id)
                .onSuccess {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            successMessage = "Plato eliminado correctamente"
                        )
                    }
                    cargarPlatos()
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Error al eliminar plato"
                        )
                    }
                }
        }
    }
}