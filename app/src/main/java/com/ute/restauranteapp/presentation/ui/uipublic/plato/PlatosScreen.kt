package com.ute.restauranteapp.presentation.ui.uipublic.plato

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ute.restauranteapp.presentation.viewmodel.CatalogViewModel

@Composable
fun PlatosScreen(
    onPlatoClick: (Int) -> Unit,
    viewModel: CatalogViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Menú",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (state.isLoading) {

            CircularProgressIndicator()

        } else {

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(state.platos) { plato ->

                    Card(
                        onClick = { onPlatoClick(plato.id) }
                    ) {

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Text(
                                text = plato.nombre,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = plato.categoriaNombre
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "$${plato.precio}"
                            )
                        }
                    }
                }
            }
        }
    }
}