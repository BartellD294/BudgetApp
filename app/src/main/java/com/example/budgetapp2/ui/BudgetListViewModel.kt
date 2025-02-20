package com.example.budgetapp2.ui

import com.example.budgetapp2.ui.theme.BudgetApp2Theme
import android.content.Context
import android.app.Application
import android.view.View
import androidx.compose.ui.platform.LocalContext
import com.example.budgetapp2.data.BudgetDatabase
import com.example.budgetapp2.data.BudgetItemsRepository

class BudgetListViewModel {
    init {
        val dao = BudgetDatabase.getDatabase(application).budgetItemDao()
    }
}