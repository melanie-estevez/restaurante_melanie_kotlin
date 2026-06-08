package com.ute.restauranteapp.presentation.ui.uipublic.plato

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ute.restauranteapp.domain.model.Plato
import com.ute.restauranteapp.presentation.viewmodel.CartViewModel
import com.ute.restauranteapp.presentation.viewmodel.PlatoDetailUiState
import com.ute.restauranteapp.presentation.viewmodel.PlatoDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlatoDetailScreen(
    platoId: Int,
    onBack: () -> Unit,
    cartViewModel: CartViewModel,
    viewModel: PlatoDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showAddedMessage by remember { mutableStateOf(false) }

    LaunchedEffect(platoId) {
        viewModel.load(platoId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del plato") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { padding ->

        when (val s = state) {
            is PlatoDetailUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is PlatoDetailUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(s.message)
                }
            }

            is PlatoDetailUiState.Success -> {
                PlatoDetailContent(
                    plato = s.plato,
                    showAddedMessage = showAddedMessage,
                    onAddToCart = {
                        cartViewModel.addItem(s.plato)
                        showAddedMessage = true
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                )
            }
        }
    }
}

@Composable
private fun PlatoDetailContent(
    plato: Plato,
    showAddedMessage: Boolean,
    onAddToCart: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.RestaurantMenu,
            contentDescription = null,
            modifier = Modifier.size(72.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = plato.nombre,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = plato.categoriaNombre,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = plato.descripcion.ifBlank { "Sin descripción disponible" },
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "$${"%.2f".format(plato.precio)}",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onAddToCart,
            enabled = plato.disponible,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (plato.disponible) {
                    "Agregar al carrito"
                } else {
                    "No disponible"
                }
            )
        }

        if (showAddedMessage) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Plato agregado al carrito",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}