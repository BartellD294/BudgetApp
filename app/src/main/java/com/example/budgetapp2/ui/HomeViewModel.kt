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
    private val barOrPieButtonIndex = MutableStateFlow(0)
    private val itemsOrCategoriesButtonIndex = MutableStateFlow(0)
    // style = bar graph -> style = 0
    // style = pie chart -> style = 1
    // type = items -> type = 0
    // type = categories -> type = 1


    //Had to use nested combine()s here because combine() only allows 5 flows directly
    var homeUiState: StateFlow<HomeUiState> =
        combine(
            combine(
                application.container.repository.getAllItems(),
                application.container.repository.getAllCategoriesExpensesOrIncomes(0),
                application.container.repository.getMaxValueExpensesOrIncomes(0),
                application.container.repository.getMaxTotalCategoryValueExpenseOrIncome(0),
                application.container.repository.getTotalValueExpensesOrIncomes(0),
            ) { budgetItemList, expenseCategoryList, maxExpenseValue, maxExpenseCategoryValueTotal, allExpensesTotal ->
                CombinedFlow1(
                    budgetItemList,
                    expenseCategoryList,
                    maxExpenseValue,
                    maxExpenseCategoryValueTotal,
                    allExpensesTotal
                )
            }, barOrPieButtonIndex, itemsOrCategoriesButtonIndex
        ) {
            combined1, barOrPieButtonIndex, itemsOrCategoriesButtonIndex ->
            HomeUiState(
                budgetItemList = combined1.budgetItemList,
                categoryList = combined1.categoryList,
                maxItemValue = combined1.maxCost,
                maxTotalCategoryValue = combined1.maxCategoryTotal,
                totalExpensesValue = combined1.totalCostOverall,
                barOrPie = barOrPieButtonIndex,
                itemsOrCategories = itemsOrCategoriesButtonIndex,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = HomeUiState()
        )


    fun updateBarOrPie(barOrPie: Int) {
        barOrPieButtonIndex.value = barOrPie
    }
    fun updateItemsOrCategories(itemsOrCategories: Int) {
        itemsOrCategoriesButtonIndex.value = itemsOrCategories
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
    val maxItemValue: Double = 0.0,
    val maxTotalCategoryValue: Double = 0.0,
    val totalExpensesValue: Double = 0.0,
    val barOrPie: Int = 0,
    val itemsOrCategories: Int = 0
)
