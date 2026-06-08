package com.ute.restauranteapp.presentation.ui.admin.categorias

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ute.restauranteapp.domain.model.CategoriaMenu
import com.ute.restauranteapp.presentation.viewmodel.admin.CategoriaAdminViewModel

@Composable
fun CategoriasAdminScreen(
    viewModel: CategoriaAdminViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showForm by remember { mutableStateOf(false) }

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
                    text = "Categorías",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Button(
                    onClick = {
                        viewModel.seleccionarCategoria(null)
                        showForm = true
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Nueva")
                }
            }

            Spacer(Modifier.height(16.dp))

            when {
                state.isLoading && state.categorias.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                state.categorias.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No hay categorías registradas")
                    }
                }

                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.categorias) { categoria ->
                            CategoriaItem(
                                categoria = categoria,
                                onEdit = {
                                    viewModel.seleccionarCategoria(categoria)
                                    showForm = true
                                },
                                onDelete = {
                                    viewModel.eliminarCategoria(categoria.id)
                                }
                            )
                        }
                    }
                }
            }
        }

        if (showForm) {
            CategoriaFormSheet(
                categoria = state.selectedCategoria,
                isLoading = state.isLoading,
                onDismiss = {
                    showForm = false
                    viewModel.seleccionarCategoria(null)
                },
                onSave = { id, nombre, descripcion ->
                    viewModel.guardarCategoria(
                        id = id,
                        nombre = nombre,
                        descripcion = descripcion,
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
private fun CategoriaItem(
    categoria: CategoriaMenu,
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
                imageVector = Icons.Default.Category,
                contentDescription = null,
                modifier = Modifier.size(38.dp)
            )

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = categoria.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = categoria.descripcion,
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