package com.uriolus.barometer.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.uriolus.barometer.features.historic.presentation.HistoricScreen
import com.uriolus.barometer.features.historic.presentation.HistoricViewModel
import com.uriolus.barometer.features.realtime.presentation.BarometerScreen
import com.uriolus.barometer.features.realtime.presentation.BarometerViewModel
import org.koin.androidx.compose.koinViewModel

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Realtime : Screen("realtime", "Realtime", Icons.Default.Home)
    object Historic : Screen("historic", "Historic", Icons.Default.DateRange)
}

val items = listOf(
    Screen.Realtime,
    Screen.Historic
)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Realtime.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Realtime.route) {
                val viewModel: BarometerViewModel = koinViewModel()
                val state by viewModel.state.collectAsState()
                BarometerScreen(data = state.barometerData)
            }
            composable(Screen.Historic.route) {
                val viewModel: HistoricViewModel = koinViewModel()
                val state by viewModel.state.collectAsState()
                HistoricScreen(state = state)
            }
        }
    }
}
