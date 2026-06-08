package com.ute.restauranteapp.presentation.ui.admin.platos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ute.restauranteapp.domain.model.Plato
import com.ute.restauranteapp.presentation.viewmodel.admin.PlatoAdminViewModel

@Composable
fun PlatosAdminScreen(
    viewModel: PlatoAdminViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showForm by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
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
                    text = "Platos",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Button(
                    onClick = {
                        viewModel.seleccionarPlato(null)
                        showForm = true
                    },
                    enabled = state.categorias.isNotEmpty()
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Nuevo")
                }
            }

            Spacer(Modifier.height(16.dp))

            if (state.error != null) {
                Text(
                    text = state.error ?: "",
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(Modifier.height(8.dp))
            }

            when {
                state.isLoading && state.platos.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                state.platos.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No hay platos registrados")
                    }
                }

                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.platos) { plato ->
                            PlatoAdminItem(
                                plato = plato,
                                onEdit = {
                                    viewModel.seleccionarPlato(plato)
                                    showForm = true
                                },
                                onDelete = {
                                    viewModel.eliminarPlato(plato.id)
                                }
                            )
                        }
                    }
                }
            }
        }

        if (showForm) {
            PlatoFormSheet(
                plato = state.selectedPlato,
                categorias = state.categorias,
                isLoading = state.isLoading,
                onDismiss = {
                    showForm = false
                    viewModel.seleccionarPlato(null)
                },
                onSave = { id, categoria, nombre, descripcion, precio, disponible ->
                    viewModel.guardarPlato(
                        id = id,
                        categoria = categoria,
                        nombre = nombre,
                        descripcion = descripcion,
                        precio = precio,
                        disponible = disponible,
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
private fun PlatoAdminItem(
    plato: Plato,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.RestaurantMenu,
                contentDescription = null,
                modifier = Modifier.size(38.dp)
            )

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = plato.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = plato.categoriaNombre,
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = "$${"%.2f".format(plato.precio)}",
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = if (plato.disponible) "Disponible" else "No disponible",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (plato.disponible) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.error
                    }
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