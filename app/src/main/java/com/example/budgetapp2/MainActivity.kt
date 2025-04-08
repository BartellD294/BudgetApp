package com.example.budgetapp2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.budgetapp2.ui.NavScreen
import com.example.budgetapp2.ui.theme.BudgetApp2Theme


sealed class Screen(val route: String, val title: String) {
    object Home: Screen("home", "Home")
    object BudgetList: Screen("budgetList", "Budget List")
    object AddItem: Screen("addItem", "Add Item")
    object Settings: Screen("settings", "Settings")
}


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        // -- Only uncomment this for debugging (if/when schema has been changed) --
        //this.deleteDatabase("budget_item_database")
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BudgetApp2Theme {
                NavScreen()
            }
        }
    }
}









