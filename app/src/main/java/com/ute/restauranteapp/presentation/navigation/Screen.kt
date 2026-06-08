package com.ute.restauranteapp.presentation.navigation

sealed class Screen(val route: String) {


    data object Login : Screen("login")
    data object Register : Screen("register")


    data object Home : Screen("home")

    data object Platos : Screen("platos")
    data class PlatoDetail(val id: Int = 0) : Screen("platos/{id}") {
        fun createRoute(id: Int) = "platos/$id"
    }

    data object Pedidos : Screen("pedidos")
    data class PedidoDetail(val id: Int = 0) : Screen("pedidos/{id}") {
        fun createRoute(id: Int) = "pedidos/$id"
    }

    data object CrearPedido : Screen("pedidos/crear")

    data object Profile : Screen("profile")


    data object AdminDashboard : Screen("admin")

    data object AdminClientes : Screen("admin/clientes")
    data class ClienteDetail(val id: Int = 0) : Screen("admin/clientes/{id}") {
        fun createRoute(id: Int) = "admin/clientes/$id"
    }

    data object AdminCategorias : Screen("admin/categorias")
    data class CategoriaDetail(val id: Int = 0) : Screen("admin/categorias/{id}") {
        fun createRoute(id: Int) = "admin/categorias/$id"
    }

    data object AdminPlatos : Screen("admin/platos")

    data object AdminPedidos : Screen("admin/pedidos")

    data object AdminUsuarios : Screen("admin/usuarios")
}