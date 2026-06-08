package com.ute.restauranteapp.presentation.ui.admin.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ute.restauranteapp.presentation.viewmodel.DashboardStats
import com.ute.restauranteapp.presentation.viewmodel.DashboardUiState
import com.ute.restauranteapp.presentation.viewmodel.DashboardViewModel
import com.ute.restauranteapp.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DashboardScreen(
    onNavigate: (String) -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val lastUpdated by viewModel.lastUpdated.collectAsStateWithLifecycle()

    when (val s = state) {
        is DashboardUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Accent)
            }
        }

        is DashboardUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "⚠ ${s.message}",
                        color = Error
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = viewModel::load,
                        colors = ButtonDefaults.buttonColors(containerColor = Accent)
                    ) {
                        Text(
                            text = "Reintentar",
                            color = AccentOnDark
                        )
                    }
                }
            }
        }

        is DashboardUiState.Success -> {
            DashboardContent(
                stats = s.stats,
                lastUpdated = lastUpdated,
                onNavigate = onNavigate,
                onRefresh = viewModel::load
            )
        }
    }
}

@Composable
private fun DashboardContent(
    stats: DashboardStats,
    lastUpdated: Long,
    onNavigate: (String) -> Unit,
    onRefresh: () -> Unit
) {
    val timeFmt = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    val timeStr = if (lastUpdated > 0) timeFmt.format(Date(lastUpdated)) else "—"
    val platosNoDisponibles = stats.totalPlatos - stats.totalPlatosActivos

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Dashboard Cafetería ☕",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Text(
                        text = "Actualizado: $timeStr",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }

                IconButton(onClick = onRefresh) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Actualizar",
                        tint = Accent
                    )
                }
            }
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                KpiCard(
                    title = "Platos disponibles",
                    value = stats.totalPlatosActivos.toString(),
                    subtitle = "$platosNoDisponibles no disponibles",
                    icon = Icons.Default.Restaurant,
                    color = Accent,
                    hasAlert = platosNoDisponibles > 0,
                    onClick = { onNavigate("admin/platos") },
                    modifier = Modifier.weight(1f)
                )

                KpiCard(
                    title = "Categorías",
                    value = stats.totalCategorias.toString(),
                    subtitle = "${stats.categoriasActivas} activas",
                    icon = Icons.Default.Category,
                    color = Info,
                    onClick = { onNavigate("admin/categorias") },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                KpiCard(
                    title = "Pedidos",
                    value = stats.totalPedidos.toString(),
                    subtitle = "${stats.pedidosPendientes} pendientes",
                    icon = Icons.Default.ReceiptLong,
                    color = Success,
                    hasAlert = stats.pedidosPendientes > 0,
                    onClick = { onNavigate("admin/pedidos") },
                    modifier = Modifier.weight(1f)
                )

                KpiCard(
                    title = "Clientes",
                    value = stats.totalClientes.toString(),
                    subtitle = "${stats.usuariosActivos} activos",
                    icon = Icons.Default.People,
                    color = Warning,
                    onClick = { onNavigate("admin/clientes") },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Surface(
                color = Surface,
                shape = MaterialTheme.shapes.large,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Pedidos por estado",
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    val estados = listOf(
                        "Pendientes" to stats.pedidosPendientes,
                        "Preparando" to stats.pedidosPreparando,
                        "Listos" to stats.pedidosListos,
                        "Entregados" to stats.pedidosEntregados
                    )

                    estados.forEach { (estado, cantidad) ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = estado,
                                color = TextSecondary
                            )

                            Text(
                                text = cantidad.toString(),
                                color = Accent,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                    }
                }
            }
        }

        item {
            Surface(
                color = Surface,
                shape = MaterialTheme.shapes.large,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Acciones rápidas",
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(
                            listOf(
                                Triple("Platos", Icons.Default.Restaurant, "admin/platos"),
                                Triple("Categorías", Icons.Default.Category, "admin/categorias"),
                                Triple("Pedidos", Icons.Default.ReceiptLong, "admin/pedidos"),
                                Triple("Clientes", Icons.Default.People, "admin/clientes")
                            )
                        ) { (label, icon, route) ->
                            Surface(
                                onClick = { onNavigate(route) },
                                color = Accent.copy(alpha = 0.1f),
                                shape = MaterialTheme.shapes.medium
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = null,
                                        tint = Accent
                                    )

                                    Spacer(modifier = Modifier.width(6.dp))

                                    Text(
                                        text = label,
                                        color = Accent,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}