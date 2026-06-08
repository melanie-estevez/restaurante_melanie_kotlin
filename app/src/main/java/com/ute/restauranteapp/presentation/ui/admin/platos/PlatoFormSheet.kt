package com.ute.restauranteapp.presentation.ui.admin.platos

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ute.restauranteapp.domain.model.CategoriaMenu
import com.ute.restauranteapp.domain.model.Plato

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlatoFormSheet(
    plato: Plato?,
    categorias: List<CategoriaMenu>,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onSave: (
        id: Int?,
        categoria: Int,
        nombre: String,
        descripcion: String,
        precio: Double,
        disponible: Boolean
    ) -> Unit
) {
    var nombre by remember(plato) { mutableStateOf(plato?.nombre ?: "") }
    var descripcion by remember(plato) { mutableStateOf(plato?.descripcion ?: "") }
    var precio by remember(plato) { mutableStateOf(plato?.precio?.toString() ?: "") }
    var disponible by remember(plato) { mutableStateOf(plato?.disponible ?: true) }
    var categoriaSeleccionada by remember(plato, categorias) {
        mutableStateOf(
            categorias.firstOrNull { it.id == plato?.categoria }
                ?: categorias.firstOrNull()
        )
    }

    var expanded by remember { mutableStateOf(false) }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = if (plato == null) "Nuevo plato" else "Editar plato",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = categoriaSeleccionada?.nombre ?: "Seleccione categoría",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoría") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categorias.forEach { categoria ->
                        DropdownMenuItem(
                            text = { Text(categoria.nombre) },
                            onClick = {
                                categoriaSeleccionada = categoria
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it },
                label = { Text("Precio") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Disponible")
                Switch(
                    checked = disponible,
                    onCheckedChange = { disponible = it }
                )
            }

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    val precioDouble = precio.toDoubleOrNull() ?: 0.0
                    val categoriaId = categoriaSeleccionada?.id ?: 0

                    onSave(
                        plato?.id,
                        categoriaId,
                        nombre.trim(),
                        descripcion.trim(),
                        precioDouble,
                        disponible
                    )
                },
                enabled = !isLoading &&
                        categoriaSeleccionada != null &&
                        nombre.isNotBlank() &&
                        precio.toDoubleOrNull() != null &&
                        (precio.toDoubleOrNull() ?: 0.0) > 0,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (plato == null) "Crear plato" else "Guardar cambios")
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}