package com.ute.restauranteapp.presentation.ui.uipublic.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ute.restauranteapp.presentation.viewmodel.CartViewModel
import com.ute.restauranteapp.presentation.viewmodel.CheckoutState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartBottomSheet(
    cartViewModel: CartViewModel,
    clienteId: Int,
    onDismiss: () -> Unit,
    onOrderSuccess: (Int) -> Unit
) {
    val items by cartViewModel.items.collectAsState()
    val total by cartViewModel.total.collectAsState()
    val checkoutState by cartViewModel.checkoutState.collectAsState()

    LaunchedEffect(checkoutState) {
        if (checkoutState is CheckoutState.Success) {
            val pedidoId = (checkoutState as CheckoutState.Success).pedidoId
            cartViewModel.clearCart()
            onOrderSuccess(pedidoId)
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Mi carrito",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))

            if (items.isEmpty()) {
                Text("Tu carrito está vacío")
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.heightIn(max = 350.dp)
                ) {
                    items(items) { item ->
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = item.plato.nombre,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Text(
                                        text = "$${"%.2f".format(item.plato.precio)}"
                                    )

                                    Text(
                                        text = "Cantidad: ${item.quantity}"
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        cartViewModel.updateQuantity(
                                            item.plato.id,
                                            item.quantity - 1
                                        )
                                    }
                                ) {
                                    Icon(Icons.Default.Remove, contentDescription = null)
                                }

                                IconButton(
                                    onClick = {
                                        cartViewModel.updateQuantity(
                                            item.plato.id,
                                            item.quantity + 1
                                        )
                                    }
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = null)
                                }

                                IconButton(
                                    onClick = {
                                        cartViewModel.removeItem(item.plato.id)
                                    }
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = null)
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Total: $${"%.2f".format(total)}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = {
                        cartViewModel.checkout(clienteId)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = checkoutState !is CheckoutState.Loading
                ) {
                    if (checkoutState is CheckoutState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Confirmar pedido")
                    }
                }

                if (checkoutState is CheckoutState.Error) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = (checkoutState as CheckoutState.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}