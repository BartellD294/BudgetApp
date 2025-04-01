package com.example.budgetapp2.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgetapp2.BudgetApplication
import com.example.budgetapp2.data.BudgetItem
import com.example.budgetapp2.data.Category
import com.example.budgetapp2.data.OfflineBudgetItemsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(application: BudgetApplication): ViewModel() {
    private val buttonIndex = MutableStateFlow(0)

    //Had to use nested combine()s here because combine() only allows 5 flows directly
    var homeUiState: StateFlow<HomeUiState> =
        combine(
            combine(
                application.container.repository.getAllItemsStream(),
                application.container.repository.getAllCategoriesStream(),
                application.container.repository.getMaxCost(),
                application.container.repository.getMaxCategoryTotal(),
                application.container.repository.getAllExpensesTotal(),
            ) { budgetItemList, categoryList, maxCost, maxCategoryTotal, allExpensesTotal ->
                CombinedFlow1(
                    budgetItemList,
                    categoryList,
                    maxCost,
                    maxCategoryTotal,
                    allExpensesTotal
                )
            }, buttonIndex
        ) {
            combined1, buttonIndex ->
            HomeUiState(
                budgetItemList = combined1.budgetItemList,
                categoryList = combined1.categoryList,
                maxCost = combined1.maxCost,
                maxCategoryTotal = combined1.maxCategoryTotal,
                totalCostOverall = combined1.totalCostOverall,
                buttonIndex = buttonIndex
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = HomeUiState()
        )


fun updateButton(index: Int) {
    buttonIndex.value = index
    //Log.i("button index", buttonIndex.value.toString())
}
}

data class CombinedFlow1(
    val budgetItemList: List<BudgetItem>,
    val categoryList: List<Category>,
    val maxCost: Double,
    val maxCategoryTotal: Double,
    val totalCostOverall: Double
)


data class HomeUiState(
    val budgetItemList: List<BudgetItem> = listOf(),
    val categoryList: List<Category> = listOf(),
    val maxCost: Double = 0.0,
    val maxCategoryTotal: Double = 0.0,
    val totalCostOverall: Double = 0.0,
    val buttonIndex: Int = 0
)
