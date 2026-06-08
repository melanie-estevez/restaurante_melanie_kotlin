package com.ute.restauranteapp.presentation.ui.uipublic.categoria

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ute.restauranteapp.domain.model.CategoriaMenu

import com.ute.restauranteapp.presentation.viewmodel.CategoriaViewModel

@Composable
fun CategoriasScreen(
    onCategoriaClick: (Int) -> Unit,
    viewModel: CategoriaViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Categorías",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            state.error != null -> {
                Text(state.error ?: "Error cargando categorías")
            }

            state.categorias.isEmpty() -> {
                Text("No hay categorías registradas")
            }

            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.categorias) { categoria ->
                        CategoriaItem(
                            categoria = categoria,
                            onClick = { onCategoriaClick(categoria.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoriaItem(
    categoria: CategoriaMenu,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Category,
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = categoria.nombre,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = categoria.descripcion,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}