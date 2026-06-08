package com.ute.restauranteapp.presentation.ui.uipublic.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ute.restauranteapp.domain.model.Plato
import com.ute.restauranteapp.presentation.viewmodel.CatalogViewModel
import com.ute.restauranteapp.presentation.viewmodel.CategoriaViewModel
import com.ute.restauranteapp.theme.*

@Composable
fun HomeScreen(
    onPlatoClick: (Int) -> Unit,
    onMenuClick: () -> Unit,
    catalogViewModel: CatalogViewModel = hiltViewModel(),
    categoriaViewModel: CategoriaViewModel = hiltViewModel()
) {
    val catalogState by catalogViewModel.state.collectAsState()
    val categoriaState by categoriaViewModel.state.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Surface2)
                    .padding(24.dp)
            ) {
                Column {
                    Text(
                        text = "Bienvenido a",
                        fontSize = 28.sp,
                        color = TextSecondary
                    )

                    Text(
                        text = "Nuestra cafetería ☕",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Accent
                    )

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text = "Platos frescos, café y postres preparados al momento.",
                        color = TextSecondary
                    )

                    Spacer(Modifier.height(16.dp))

                    Button(
                        onClick = onMenuClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Accent)
                    ) {
                        Text("Ver menú")
                        Spacer(Modifier.width(8.dp))
                        Icon(Icons.Default.ArrowForward, contentDescription = null)
                    }
                }
            }
        }

        if (categoriaState.categorias.isNotEmpty()) {
            item {
                SectionHeader("Categorías", onMenuClick)
            }

            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(categoriaState.categorias.take(6)) { categoria ->
                        CategoryChip(
                            name = categoria.nombre,
                            onClick = onMenuClick
                        )
                    }
                }
            }
        }

        item {
            SectionHeader("Recomendados", onMenuClick)
        }

        if (catalogState.isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Accent)
                }
            }
        } else {
            val chunks = catalogState.platos.take(4).chunked(2)

            items(chunks) { row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    row.forEach { plato ->
                        PlatoCard(
                            plato = plato,
                            onClick = { onPlatoClick(plato.id) },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    if (row.size == 1) {
                        Spacer(Modifier.weight(1f))
                    }
                }

                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun PlatoCard(
    plato: Plato,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.large,
        color = Surface,
        modifier = modifier
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .background(Surface2),
                contentAlignment = Alignment.Center
            ) {
                Text("🍽️", fontSize = 32.sp)

                if (!plato.disponible) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .background(Error.copy(alpha = 0.85f))
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No disponible",
                            color = MaterialTheme.colorScheme.onError,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = plato.categoriaNombre,
                    color = Accent,
                    style = MaterialTheme.typography.labelSmall
                )

                Spacer(Modifier.height(2.dp))

                Text(
                    text = plato.nombre,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                    maxLines = 2
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = "$${"%.2f".format(plato.precio)}",
                    fontWeight = FontWeight.Bold,
                    color = Accent
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    onSeeAll: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        TextButton(onClick = onSeeAll) {
            Text("Ver todo", color = Accent)
        }
    }
}

@Composable
private fun CategoryChip(
    name: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        color = Surface2,
        modifier = Modifier.width(120.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("☕", fontSize = 24.sp)

            Spacer(Modifier.height(6.dp))

            Text(
                text = name,
                color = TextPrimary,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}