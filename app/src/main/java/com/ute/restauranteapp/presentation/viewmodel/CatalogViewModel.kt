package com.ute.restauranteapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ute.restauranteapp.domain.model.Plato
import com.ute.restauranteapp.domain.repository.PlatoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CatalogUiState(
    val platos: List<Plato> = emptyList(),
    val total: Int = 0,
    val page: Int = 1,
    val search: String = "",
    val category: String? = null,
    val ordering: String = "",
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasMore: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val platoRepository: PlatoRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CatalogUiState())
    val state = _state.asStateFlow()

    init {
        load(reset = true)
    }

    fun load(reset: Boolean = true) {
        viewModelScope.launch {
            val currentState = _state.value

            _state.update {
                it.copy(
                    isLoading = reset,
                    isLoadingMore = !reset,
                    error = null
                )
            }

            try {
                val result = platoRepository.getPlatos()

                result
                    .onSuccess { platos ->
                        val filteredList = platos
                            .filter { plato ->
                                currentState.search.isBlank() ||
                                        plato.nombre.contains(
                                            currentState.search,
                                            ignoreCase = true
                                        )
                            }

                        val orderedList = when (currentState.ordering) {
                            "nombre" -> filteredList.sortedBy { it.nombre }
                            "-nombre" -> filteredList.sortedByDescending { it.nombre }
                            "precio" -> filteredList.sortedBy { it.precio }
                            "-precio" -> filteredList.sortedByDescending { it.precio }
                            else -> filteredList
                        }

                        _state.update { s ->
                            s.copy(
                                platos = orderedList,
                                total = orderedList.size,
                                hasMore = false,
                                isLoading = false,
                                isLoadingMore = false,
                                page = 1,
                                error = null
                            )
                        }
                    }
                    .onFailure { e ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                isLoadingMore = false,
                                error = e.message ?: "Error cargando platos"
                            )
                        }
                    }

            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        isLoadingMore = false,
                        error = e.message ?: "Error cargando platos"
                    )
                }
            }
        }
    }

    fun setSearch(search: String) {
        _state.update {
            it.copy(search = search)
        }
        load(reset = true)
    }

    fun setCategory(category: String?) {
        _state.update {
            it.copy(category = category)
        }
        load(reset = true)
    }

    fun setOrdering(ordering: String) {
        _state.update {
            it.copy(ordering = ordering)
        }
        load(reset = true)
    }

    fun loadMore() {
        load(reset = false)
    }
}