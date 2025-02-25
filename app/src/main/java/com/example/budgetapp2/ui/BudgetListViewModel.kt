package com.example.budgetapp2.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgetapp2.data.BudgetItem
import com.example.budgetapp2.data.BudgetItemsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class BudgetListViewModel(
    repository: BudgetItemsRepository
): ViewModel() {
    val budgetListUiState: StateFlow<BudgetListUiState> =
        repository.getAllItemsStream().map { BudgetListUiState(it) }
            .stateIn(
                scope= viewModelScope,
                started=SharingStarted.WhileSubscribed(5_000L),
                initialValue=BudgetListUiState()
            )
}


data class BudgetListUiState(val budgetItemList: List<BudgetItem> = listOf())