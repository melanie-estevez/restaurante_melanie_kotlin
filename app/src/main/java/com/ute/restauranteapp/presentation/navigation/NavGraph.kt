package com.ute.restauranteapp.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.ute.restauranteapp.presentation.ui.admin.categorias.CategoriasAdminScreen
import com.ute.restauranteapp.presentation.ui.admin.clientes.ClientesAdminScreen
import com.ute.restauranteapp.presentation.ui.admin.dashboard.AdminScaffold
import com.ute.restauranteapp.presentation.ui.admin.pedidos.PedidosAdminScreen
import com.ute.restauranteapp.presentation.ui.admin.platos.PlatosAdminScreen
import com.ute.restauranteapp.presentation.ui.admin.users.UsersAdminScreen
import com.ute.restauranteapp.presentation.ui.auth.LoginScreen
import com.ute.restauranteapp.presentation.ui.auth.RegisterScreen
import com.ute.restauranteapp.presentation.ui.client.pedidos.PedidoDetailScreen
import com.ute.restauranteapp.presentation.ui.client.pedidos.PedidosScreen
import com.ute.restauranteapp.presentation.ui.client.profile.ProfileScreen
import com.ute.restauranteapp.presentation.ui.uipublic.cart.CartBottomSheet
import com.ute.restauranteapp.presentation.ui.uipublic.home.HomeScreen
import com.ute.restauranteapp.presentation.ui.uipublic.plato.PlatoDetailScreen
import com.ute.restauranteapp.presentation.ui.uipublic.plato.PlatosScreen
import com.ute.restauranteapp.presentation.viewmodel.AuthViewModel
import com.ute.restauranteapp.presentation.viewmodel.CartViewModel
import com.ute.restauranteapp.theme.Surface

@Composable
fun NavGraph(
    authViewModel: AuthViewModel,
    cartViewModel: CartViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val cartCount by cartViewModel.totalItems.collectAsState()
    var showCart by remember { mutableStateOf(false) }
    val currentUser by authViewModel.currentUser.collectAsState()

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val showBottomBar = currentRoute in listOf(
        Screen.Home.route,
        Screen.Platos.route,
        Screen.Pedidos.route,
        Screen.Profile.route
    )

    Scaffold(
        containerColor = Surface,
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(
                    navController = navController,
                    cartCount = cartCount,
                    onCartClick = { showCart = true }
                )
            }
        }
    ) { innerPadding ->

        if (showCart) {

            LaunchedEffect(currentUser) {
                android.util.Log.d(
                    "AUTH_DEBUG",
                    "USER=$currentUser"
                )
                android.util.Log.d(
                    "AUTH_DEBUG",
                    "CLIENTE_ID=${currentUser?.clienteId}"
                )
            }

            val clienteId = currentUser?.clienteId

            if (clienteId == null) {

                android.util.Log.e(
                    "AUTH_DEBUG",
                    "CLIENTE_ID NULL"
                )

                showCart = false

            } else {

                CartBottomSheet(
                    cartViewModel = cartViewModel,
                    clienteId = clienteId,
                    onDismiss = { showCart = false },
                    onOrderSuccess = {
                        showCart = false
                        navController.navigate(Screen.Pedidos.route)
                    }
                )
            }
        }

        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onPlatoClick = { id ->
                        navController.navigate(Screen.PlatoDetail().createRoute(id))
                    },
                    onMenuClick = {
                        navController.navigate(Screen.Platos.route)
                    }
                )
            }

            composable(Screen.Platos.route) {
                PlatosScreen(
                    onPlatoClick = { id ->
                        navController.navigate(Screen.PlatoDetail().createRoute(id))
                    }
                )
            }

            composable(
                route = Screen.PlatoDetail().route,
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStackEntry ->
                val platoId = backStackEntry.arguments?.getInt("id") ?: return@composable
                PlatoDetailScreen(
                    platoId = platoId,
                    onBack = { navController.popBackStack() },
                    cartViewModel = cartViewModel
                )
            }

            composable(Screen.Pedidos.route) {
                PedidosScreen(
                    onPedidoClick = { id ->
                        navController.navigate(Screen.PedidoDetail().createRoute(id))
                    }
                )
            }

            composable(
                route = Screen.PedidoDetail().route,
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStackEntry ->
                val pedidoId = backStackEntry.arguments?.getInt("id") ?: return@composable
                PedidoDetailScreen(
                    pedidoId = pedidoId,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Profile.route) {
                ProfileScreen(
                    authViewModel = authViewModel,
                    onLogout = {
                        authViewModel.logout()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Login.route) {
                LoginScreen(
                    onLoginSuccess = { isStaff ->
                        navController.navigate(
                            if (isStaff) Screen.AdminDashboard.route else Screen.Home.route
                        ) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = {
                        navController.navigate(Screen.Register.route)
                    },
                    viewModel = authViewModel
                )
            }

            composable(Screen.Register.route) {
                RegisterScreen(
                    onRegisterSuccess = { isStaff ->
                        navController.navigate(
                            if (isStaff) Screen.AdminDashboard.route else Screen.Home.route
                        ) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onNavigateToLogin = { navController.popBackStack() },
                    viewModel = authViewModel
                )
            }

            composable(Screen.AdminDashboard.route) {
                AdminRoute(
                    currentRoute = Screen.AdminDashboard.route,
                    title = "Dashboard",
                    authViewModel = authViewModel,
                    currentUser = currentUser,
                    navController = navController
                ) {
                    Box(modifier = Modifier.padding(it))
                }
            }

            composable(Screen.AdminClientes.route) {
                AdminRoute(
                    currentRoute = Screen.AdminClientes.route,
                    title = "Clientes",
                    authViewModel = authViewModel,
                    currentUser = currentUser,
                    navController = navController
                ) { padding ->
                    Box(modifier = Modifier.padding(padding)) {
                        ClientesAdminScreen()
                    }
                }
            }

            composable(Screen.AdminCategorias.route) {
                AdminRoute(
                    currentRoute = Screen.AdminCategorias.route,
                    title = "Categorías",
                    authViewModel = authViewModel,
                    currentUser = currentUser,
                    navController = navController
                ) { padding ->
                    Box(modifier = Modifier.padding(padding)) {
                        CategoriasAdminScreen()
                    }
                }
            }

            composable(Screen.AdminPlatos.route) {
                AdminRoute(
                    currentRoute = Screen.AdminPlatos.route,
                    title = "Platos",
                    authViewModel = authViewModel,
                    currentUser = currentUser,
                    navController = navController
                ) { padding ->
                    Box(modifier = Modifier.padding(padding)) {
                        PlatosAdminScreen()
                    }
                }
            }

            composable(Screen.AdminPedidos.route) {
                AdminRoute(
                    currentRoute = Screen.AdminPedidos.route,
                    title = "Pedidos",
                    authViewModel = authViewModel,
                    currentUser = currentUser,
                    navController = navController
                ) { padding ->
                    Box(modifier = Modifier.padding(padding)) {
                        PedidosAdminScreen()
                    }
                }
            }

            composable(Screen.AdminUsuarios.route) {
                AdminRoute(
                    currentRoute = Screen.AdminUsuarios.route,
                    title = "Usuarios",
                    authViewModel = authViewModel,
                    currentUser = currentUser,
                    navController = navController
                ) { padding ->
                    Box(modifier = Modifier.padding(padding)) {
                        UsersAdminScreen()
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminRoute(
    currentRoute: String,
    title: String,
    authViewModel: AuthViewModel,
    currentUser: com.ute.restauranteapp.domain.model.LoggedUser?,
    navController: androidx.navigation.NavHostController,
    content: @Composable (androidx.compose.foundation.layout.PaddingValues) -> Unit
) {
    AdminScaffold(
        currentRoute = currentRoute,
        user = currentUser,
        title = title,
        onNavClick = { route ->
            navController.navigate(route) {
                launchSingleTop = true
            }
        },
        onStoreClick = {
            navController.navigate(Screen.Home.route)
        },
        onLogout = {
            authViewModel.logout()
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        },
        content = content
    )
}