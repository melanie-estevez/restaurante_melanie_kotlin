package com.ute.restauranteapp.presentation.ui.client.pedidos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ute.restauranteapp.presentation.viewmodel.PedidoViewModel

@Composable
fun PedidosScreen(
    onPedidoClick: (Int) -> Unit,
    viewModel: PedidoViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarPedidos()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Mis pedidos",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.error != null -> {
                Text(
                    text = uiState.error ?: "Error al cargar pedidos"
                )
            }

            uiState.pedidos.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No tienes pedidos registrados")
                }
            }

            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.pedidos) { pedido ->
                        Card(
                            onClick = {
                                onPedidoClick(pedido.id)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Pedido #${pedido.id}",
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "Cliente: ${pedido.clienteNombre}"
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "Estado: ${pedido.estado.label}"
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "Total: $${"%.2f".format(pedido.total)}"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}