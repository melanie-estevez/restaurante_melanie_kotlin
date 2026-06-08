package com.ute.restauranteapp.presentation.ui.admin.clientes

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ute.restauranteapp.domain.model.Cliente

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteFormSheet(
    cliente: Cliente?,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onSave: (
        id: Int?,
        nombreCompleto: String,
        telefono: String,
        correo: String
    ) -> Unit
) {
    var nombreCompleto by remember(cliente) { mutableStateOf(cliente?.nombreCompleto ?: "") }
    var telefono by remember(cliente) { mutableStateOf(cliente?.telefono ?: "") }
    var correo by remember(cliente) { mutableStateOf(cliente?.correo ?: "") }

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = if (cliente == null) "Nuevo cliente" else "Editar cliente",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = nombreCompleto,
                onValueChange = { nombreCompleto = it },
                label = { Text("Nombre completo") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    onSave(
                        cliente?.id,
                        nombreCompleto.trim(),
                        telefono.trim(),
                        correo.trim()
                    )
                },
                enabled = !isLoading &&
                        nombreCompleto.isNotBlank() &&
                        telefono.isNotBlank() &&
                        correo.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (cliente == null) "Crear cliente" else "Guardar cambios")
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}