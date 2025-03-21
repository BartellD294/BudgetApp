package com.example.budgetapp2.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.budgetapp2.Screen
import com.example.budgetapp2.ui.theme.BudgetApp2Theme

@Composable
fun NavScreen() {

    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController, listOf(
                    BottomNavigationItem(
                        title = "Home",
                        icon = Icons.Default.Home,
                        route = Screen.Home.route
                    ),
                    BottomNavigationItem(
                        title = "Budget List",
                        icon = Icons.Default.Menu,
                        route = Screen.BudgetList.route
                    ),
                    BottomNavigationItem(
                        title = "Add Item",
                        icon = Icons.Default.Add,
                        route = Screen.AddItem.route
                    ),
                    BottomNavigationItem(
                        title = "Settings",
                        icon = Icons.Default.Settings,
                        route = Screen.Settings.route
                    )
                )
            )
        }
    ) {
        innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen()
            }
            composable(Screen.BudgetList.route) {
                BudgetListScreen(navController = navController)
            }
            composable(
                route = "EditItem/{id}") {
                navBackStackEntry ->
                val id = navBackStackEntry.arguments?.getString("id")
                AddItemScreen(popBackStack = { navController.popBackStack() }, id = id)
            }
            composable(Screen.AddItem.route) {
                AddItemScreen(popBackStack = { navController.popBackStack() })
            }
            composable(Screen.Settings.route) {
                SettingsScreen()
            }
        }
    }
}

data class BottomNavigationItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun BottomNavigationBar(
    navController: NavController,
    items: List<BottomNavigationItem>
) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
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


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BudgetApp2Theme {
        NavScreen()
    }
}
