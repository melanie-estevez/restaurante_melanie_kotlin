package com.ute.restauranteapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ute.restauranteapp.domain.model.Plato
import com.ute.restauranteapp.domain.repository.CategoriaMenuRepository
import com.ute.restauranteapp.domain.repository.PedidoRepository
import com.ute.restauranteapp.domain.repository.PlatoRepository
import com.ute.restauranteapp.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardStats(
    val totalPlatosActivos: Int = 0,
    val totalPlatos: Int = 0,

    val totalCategorias: Int = 0,
    val categoriasActivas: Int = 0,

    val totalPedidos: Int = 0,
    val pedidosPendientes: Int = 0,
    val pedidosPreparando: Int = 0,
    val pedidosListos: Int = 0,
    val pedidosEntregados: Int = 0,

    val totalClientes: Int = 0,
    val usuariosActivos: Int = 0,

    val platosMasPedidos: List<Plato> = emptyList()
)

sealed interface DashboardUiState {
    data object Loading : DashboardUiState
    data class Success(val stats: DashboardStats) : DashboardUiState
    data class Error(val message: String) : DashboardUiState
}

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val platoRepository: PlatoRepository,
    private val categoriaRepository: CategoriaMenuRepository,
    private val pedidoRepository: PedidoRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state =
        MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)

    val state = _state.asStateFlow()

    private val _lastUpdated = MutableStateFlow(0L)
    val lastUpdated = _lastUpdated.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {

            _state.value = DashboardUiState.Loading

            try {

                val platosDeferred =
                    async { platoRepository.getPlatos() }

                val categoriasDeferred =
                    async { categoriaRepository.getCategorias() }

                val pedidosStatsDeferred =
                    async { pedidoRepository.getStats() }

                val userStatsDeferred =
                    async { userRepository.getStats() }

                val platos =
                    platosDeferred.await().getOrElse { emptyList() }

                val categorias =
                    categoriasDeferred.await().getOrElse { emptyList() }

                val pedidosStats =
                    pedidosStatsDeferred.await().getOrElse { emptyMap() }

                val userStats =
                    userStatsDeferred.await().getOrElse { emptyMap() }

                val stats = DashboardStats(

                    totalPlatosActivos =
                        platos.count { it.disponible },

                    totalPlatos =
                        platos.size,

                    totalCategorias =
                        categorias.size,

                    categoriasActivas =
                        categorias.size,

                    totalPedidos =
                        (pedidosStats["total"] as? Int) ?: 0,

                    pedidosPendientes =
                        (pedidosStats["pendientes"] as? Int) ?: 0,

                    pedidosPreparando =
                        (pedidosStats["preparando"] as? Int) ?: 0,

                    pedidosListos =
                        (pedidosStats["listos"] as? Int) ?: 0,

                    pedidosEntregados =
                        (pedidosStats["entregados"] as? Int) ?: 0,

                    totalClientes =
                        (userStats["total"] as? Int) ?: 0,

                    usuariosActivos =
                        (userStats["active"] as? Int) ?: 0,

                    platosMasPedidos =
                        platos.take(5)
                )

                _state.value =
                    DashboardUiState.Success(stats)

                _lastUpdated.value =
                    System.currentTimeMillis()

            } catch (e: Exception) {

                _state.value =
                    DashboardUiState.Error(
                        e.message ?: "Error cargando dashboard"
                    )
            }
        }
    }
}