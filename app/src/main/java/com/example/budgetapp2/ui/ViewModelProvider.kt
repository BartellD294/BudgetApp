package com.example.budgetapp2.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.budgetapp2.BudgetApplication
import com.example.budgetapp2.data.AppDataContainer
import com.example.budgetapp2.ui.BudgetListViewModel

object ViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(
                budgetApplication()
            )
        }
        initializer {
            BudgetListViewModel(
                budgetApplication()
            )
        }
        initializer {
            AddItemViewModel(
                budgetApplication()
            )
        }
        initializer {
            SettingsViewModel(
                budgetApplication(),
            )
        }
    }
}

fun CreationExtras.budgetApplication(): BudgetApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as BudgetApplication)