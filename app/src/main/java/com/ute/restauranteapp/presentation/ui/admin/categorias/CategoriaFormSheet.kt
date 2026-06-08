package com.ute.restauranteapp.presentation.ui.admin.categorias

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ute.restauranteapp.domain.model.CategoriaMenu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriaFormSheet(
    categoria: CategoriaMenu?,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onSave: (
        id: Int?,
        nombre: String,
        descripcion: String
    ) -> Unit
) {
    var nombre by remember(categoria) { mutableStateOf(categoria?.nombre ?: "") }
    var descripcion by remember(categoria) { mutableStateOf(categoria?.descripcion ?: "") }

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = if (categoria == null) "Nueva categoría" else "Editar categoría",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))

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

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    onSave(
                        categoria?.id,
                        nombre.trim(),
                        descripcion.trim()
                    )
                },
                enabled = !isLoading && nombre.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (categoria == null) "Crear categoría" else "Guardar cambios")
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}