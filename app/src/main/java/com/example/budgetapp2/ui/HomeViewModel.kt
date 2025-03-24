package com.example.budgetapp2.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgetapp2.BudgetApplication
import com.example.budgetapp2.data.AppDataContainer
import com.example.budgetapp2.data.BudgetItem
import com.example.budgetapp2.data.BudgetItemsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(application: BudgetApplication): ViewModel() {
    val homeUiState: StateFlow<HomeUiState> =
        application.container.repository.getAllItemsStream().map { HomeUiState(it) }
            .stateIn(
                scope= viewModelScope,
                started= SharingStarted.WhileSubscribed(5_000L),
                initialValue= HomeUiState()
            )
}

data class HomeUiState(val budgetItemList: List<BudgetItem> = listOf())