package com.ute.restauranteapp.presentation.ui.admin.clientes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ute.restauranteapp.domain.model.Cliente
import com.ute.restauranteapp.presentation.viewmodel.admin.ClienteAdminViewModel

@Composable
fun ClientesAdminScreen(
    viewModel: ClienteAdminViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showForm by remember { mutableStateOf(false) }

    LaunchedEffect(state.successMessage, state.error) {
        if (state.successMessage != null || state.error != null) {
            viewModel.limpiarMensajes()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Clientes",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Button(
                    onClick = {
                        viewModel.seleccionarCliente(null)
                        showForm = true
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Nuevo")
                }
            }

            Spacer(Modifier.height(16.dp))

            when {
                state.isLoading && state.clientes.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                state.clientes.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No hay clientes registrados")
                    }
                }

                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.clientes) { cliente ->
                            ClienteItem(
                                cliente = cliente,
                                onEdit = {
                                    viewModel.seleccionarCliente(cliente)
                                    showForm = true
                                },
                                onDelete = {
                                    viewModel.eliminarCliente(cliente.id)
                                }
                            )
                        }
                    }
                }
            }
        }

        if (showForm) {
            ClienteFormSheet(
                cliente = state.selectedCliente,
                isLoading = state.isLoading,
                onDismiss = {
                    showForm = false
                    viewModel.seleccionarCliente(null)
                },
                onSave = { id, nombreCompleto, telefono, correo ->
                    viewModel.guardarCliente(
                        id = id,
                        nombreCompleto = nombreCompleto,
                        telefono = telefono,
                        correo = correo,
                        onSuccess = {
                            showForm = false
                        }
                    )
                }
            )
        }
    }
}

@Composable
private fun ClienteItem(
    cliente: Cliente,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(38.dp)
            )

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = cliente.nombreCompleto,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = cliente.correo,
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = cliente.telefono,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Editar")
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
            }
        }
    }
}