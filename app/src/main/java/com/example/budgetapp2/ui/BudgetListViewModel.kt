package com.example.budgetapp2.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgetapp2.BudgetApplication
import com.example.budgetapp2.data.BudgetItem
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class BudgetListViewModel(val application: BudgetApplication): ViewModel() {

    val budgetListUiState: StateFlow<BudgetListUiState> =
        application.container.repository.getAllItemsStream().map { BudgetListUiState(it) }
        //application.container.repository.getAllItemsByCategory().map { BudgetListUiState(it) }
            .stateIn(
                scope= viewModelScope,
                started=SharingStarted.WhileSubscribed(5_000L),
                initialValue=BudgetListUiState()
            )
}


public data class BudgetListUiState(val budgetItemList: List<BudgetItem> = listOf())
//public data class BudgetListUiState(val budgetItemsByCategories: List<List<BudgetItem>> = listOf())