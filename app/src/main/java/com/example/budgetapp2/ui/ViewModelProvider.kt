package com.example.budgetapp2.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.budgetapp2.BudgetApplication
import com.example.budgetapp2.data.AppContainer
import com.example.budgetapp2.ui.BudgetListViewModel

object ViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            BudgetListViewModel(
                budgetApplication().container.repository
            )
        }
        initializer {
            AddItemViewModel(
                budgetApplication().container.repository
            )
        }
    }
}

fun CreationExtras.budgetApplication(): BudgetApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as BudgetApplication)