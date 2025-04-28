package com.example.budgetapp2.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgetapp2.BudgetApplication
import com.example.budgetapp2.data.BudgetItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BudgetListViewModel(private val application: BudgetApplication): ViewModel() {
    private val expensesOrIncomes = MutableStateFlow(0)
    private val showQuantity = MutableStateFlow(true)
    private val showFrequency = MutableStateFlow(false)
    private val showCostPerWeek = MutableStateFlow(false)
    private val numFilters = MutableStateFlow(1)

    val budgetListUiState: StateFlow<BudgetListUiState> =
        combine(
            combine(
                showQuantity,
                showFrequency,
                showCostPerWeek,
                numFilters
            ) {
              showQuantity, showFrequency, showCostPerWeek, numFilters ->
                CombinedFilterFlow(
                    showQuantity,
                    showFrequency,
                    showCostPerWeek,
                    numFilters
                )
            },
            application.container.repository.getAllItems(),
            application.container.repository.getAllCategoryNames(),
            expensesOrIncomes
        ) { filter, budgetItemList, categoryList, expensesOrIncomes->
            BudgetListUiState(
                budgetItemList,
                categoryList,
                expensesOrIncomes,
                filter.showQuantity,
                filter.showFrequency,
                filter.showCostPerWeek,
                filter.numFilters)
        }
        //application.container.repository.getAllItemsStream().map { BudgetListUiState(it) }
        //application.container.repository.getAllItemsByCategory().map { BudgetListUiState(it) }
            .stateIn(
                scope= viewModelScope,
                started=SharingStarted.WhileSubscribed(5_000L),
                initialValue=BudgetListUiState()
            )
    fun removeItem(item: BudgetItem) {
        viewModelScope.launch {
            application.container.repository.deleteItem(item)
        }
    }
    fun updateButton(index: Int) {
        expensesOrIncomes.value = index
    }

    fun updateShowQuantity(checked: Boolean) {
        showQuantity.value = checked
        updateNumFilters()
    }
    fun updateShowFrequency(checked: Boolean) {
        showFrequency.value = checked
        updateNumFilters()
    }
    fun updateShowCostPerWeek(checked: Boolean) {
        showCostPerWeek.value = checked
        updateNumFilters()
    }
    private fun updateNumFilters() {
        var num = 0
        if (showQuantity.value) {
            num++
        }
        if (showFrequency.value) {
            num++
            }
        if (showCostPerWeek.value) {
            num++
        }
        numFilters.value = num
    }
    fun updateApis() {
        viewModelScope.launch {
            application.container.repository.updateItemsWithApiIds()
        }
    }
}

data class CombinedFilterFlow(
    val showQuantity: Boolean,
    val showFrequency: Boolean,
    val showCostPerWeek: Boolean,
    val numFilters: Int
)

data class BudgetListUiState(
    val budgetItemList: List<BudgetItem> = listOf(),
    val categoryList: List<String> = listOf(),
    val expensesOrIncomes: Int = 0,
    val showQuantity: Boolean = true,
    val showFrequency: Boolean = false,
    val showCostPerWeek: Boolean = false,
    val numFilters: Int = 1
)
//public data class BudgetListUiState(val budgetItemsByCategories: List<List<BudgetItem>> = listOf())