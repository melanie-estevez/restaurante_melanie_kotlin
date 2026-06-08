package com.ute.restauranteapp.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.RestaurantMenu
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ute.restauranteapp.theme.Accent
import com.ute.restauranteapp.theme.Error
import com.ute.restauranteapp.theme.Surface
import com.ute.restauranteapp.theme.TextSecondary

data class BottomNavItem(
    val screen: Screen,
    val label: String,
    val icon: ImageVector,
    val iconSelected: ImageVector,
    val badgeCount: Int = 0
)

@Composable
fun BottomNavBar(
    navController: NavController,
    cartCount: Int,
    onCartClick: () -> Unit
) {
    val items = listOf(
        BottomNavItem(
            screen = Screen.Home,
            label = "Inicio",
            icon = Icons.Outlined.Home,
            iconSelected = Icons.Filled.Home
        ),
        BottomNavItem(
            screen = Screen.Platos,
            label = "Platos",
            icon = Icons.Outlined.RestaurantMenu,
            iconSelected = Icons.Filled.RestaurantMenu
        ),
        BottomNavItem(
            screen = Screen.CrearPedido,
            label = "Carrito",
            icon = Icons.Outlined.ShoppingCart,
            iconSelected = Icons.Filled.ShoppingCart,
            badgeCount = cartCount
        ),
        BottomNavItem(
            screen = Screen.Pedidos,
            label = "Pedidos",
            icon = Icons.Outlined.ReceiptLong,
            iconSelected = Icons.Filled.ReceiptLong
        ),
        BottomNavItem(
            screen = Screen.Profile,
            label = "Perfil",
            icon = Icons.Outlined.Person,
            iconSelected = Icons.Filled.Person
        )
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Surface,
        tonalElevation = 6.dp
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.screen.route

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (item.screen == Screen.CrearPedido) {
                        onCartClick()
                    } else {
                        navController.navigate(item.screen.route) {
                            popUpTo(Screen.Home.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    if (item.badgeCount > 0) {
                        BadgedBox(
                            badge = {
                                Badge(containerColor = Error) {
                                    Text(
                                        text = if (item.badgeCount > 99) {
                                            "99+"
                                        } else {
                                            item.badgeCount.toString()
                                        }
                                    )
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (isSelected) item.iconSelected else item.icon,
                                contentDescription = item.label
                            )
                        }
                    } else {
                        Icon(
                            imageVector = if (isSelected) item.iconSelected else item.icon,
                            contentDescription = item.label
                        )
                    }
                },
                label = {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Accent,
                    selectedTextColor = Accent,
                    indicatorColor = Accent.copy(alpha = 0.12f),
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextSecondary
                )
            )
        }
    }
}