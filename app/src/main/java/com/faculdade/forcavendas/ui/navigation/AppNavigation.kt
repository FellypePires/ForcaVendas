package com.faculdade.forcavendas.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.faculdade.forcavendas.data.Repository
import com.faculdade.forcavendas.ui.screens.ClienteScreen
import com.faculdade.forcavendas.ui.screens.EnderecoScreen
import com.faculdade.forcavendas.ui.screens.ItemScreen
import com.faculdade.forcavendas.ui.screens.PedidoScreen
import com.faculdade.forcavendas.ui.viewmodel.CadastroViewModel
import com.faculdade.forcavendas.ui.viewmodel.PedidoViewModel
import com.faculdade.forcavendas.ui.viewmodel.ViewModelFactory

enum class Destino(val rota: String, val titulo: String, val icone: ImageVector) {
    PEDIDO("pedido", "Pedido", Icons.Default.ShoppingCart),
    CLIENTES("clientes", "Clientes", Icons.Default.Person),
    ENDERECOS("enderecos", "Enderecos", Icons.Default.LocationOn),
    ITENS("itens", "Itens", Icons.Default.Home)
}

@Composable
fun AppNavigation(repository: Repository) {
    val navController = rememberNavController()
    val factory = ViewModelFactory(repository)
    val cadastroViewModel: CadastroViewModel = viewModel(factory = factory)
    val pedidoViewModel: PedidoViewModel = viewModel(factory = factory)

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val destinoAtual = navBackStackEntry?.destination
                Destino.entries.forEach { destino ->
                    val selecionado = destinoAtual?.hierarchy?.any { it.route == destino.rota } == true
                    NavigationBarItem(
                        selected = selecionado,
                        onClick = {
                            navController.navigate(destino.rota) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(destino.icone, contentDescription = destino.titulo) },
                        label = { Text(destino.titulo) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Destino.PEDIDO.rota,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Destino.PEDIDO.rota) { PedidoScreen(pedidoViewModel) }
            composable(Destino.CLIENTES.rota) { ClienteScreen(cadastroViewModel) }
            composable(Destino.ENDERECOS.rota) { EnderecoScreen(cadastroViewModel) }
            composable(Destino.ITENS.rota) { ItemScreen(cadastroViewModel) }
        }
    }
}
