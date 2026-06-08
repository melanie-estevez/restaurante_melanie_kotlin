package com.ute.restauranteapp.presentation.ui.admin.pedidos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ute.restauranteapp.domain.model.Pedido
import com.ute.restauranteapp.domain.model.PedidoEstado
import com.ute.restauranteapp.presentation.viewmodel.admin.PedidoAdminViewModel

@Composable
fun PedidosAdminScreen(
    viewModel: PedidoAdminViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Pedidos",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(16.dp))

        when {
            state.isLoading && state.pedidos.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            state.error != null -> {
                Text(
                    text = state.error ?: "Error",
                    color = MaterialTheme.colorScheme.error
                )
            }

            state.pedidos.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay pedidos registrados")
                }
            }

            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.pedidos) { pedido ->
                        PedidoAdminItem(
                            pedido = pedido,
                            onEstadoChange = { estado ->
                                viewModel.cambiarEstado(pedido, estado)
                            },
                            onDelete = {
                                viewModel.eliminarPedido(pedido.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PedidoAdminItem(
    pedido: Pedido,
    onEstadoChange: (PedidoEstado) -> Unit,
    onDelete: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ReceiptLong,
                    contentDescription = null,
                    modifier = Modifier.size(38.dp)
                )

                Spacer(Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Pedido #${pedido.id}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Cliente: ${pedido.clienteNombre}",
                        style = MaterialTheme.typography.bodySmall
                    )

                    Text(
                        text = "Total: $${"%.2f".format(pedido.total)}",
                        style = MaterialTheme.typography.bodySmall
                    )

                    Text(
                        text = "Fecha: ${pedido.fechaPedido}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                }
            }

            Spacer(Modifier.height(12.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = pedido.estado.label,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Estado") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    PedidoEstado.entries.forEach { estado ->
                        DropdownMenuItem(
                            text = { Text(estado.label) },
                            onClick = {
                                expanded = false
                                onEstadoChange(estado)
                            }
                        )
                    }
                }
            }
        }
    }
}