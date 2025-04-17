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
                application.container.repository.getAllExpensesOrIncomes(0),
                application.container.repository.getAllExpensesOrIncomes(1),
                application.container.repository.getAllCategoriesExpensesOrIncomes(0),
                application.container.repository.getAllCategoriesExpensesOrIncomes(1),
                //application.container.repository.getMaxValueExpensesOrIncomes(0),
                application.container.repository.getMaxDailyValueExpensesOrIncomes(0),
            ) { expenseList, incomeList, expenseCategoryList, incomeCategoryList, maxExpenseDailyValue ->
                CombinedFlow1(
                    expenseList,
                    incomeList,
                    expenseCategoryList,
                    incomeCategoryList,
                    maxExpenseDailyValue,
                )
            },
            combine(
                //application.container.repository.getTotalValueExpensesOrIncomes(0),
                application.container.repository.getTotalDailyValueExpensesOrIncomes(0),
                //application.container.repository.getMaxTotalCategoryValueExpenseOrIncome(0),
                application.container.repository.getMaxTotalDailyCategoryValueExpenseOrIncome(0),
                barOrPieButtonIndex,
                itemsOrCategoriesButtonIndex,
                application.container.repository.getTotalDailyValueExpensesOrIncomes(1)
            ) { totalExpensesDailyValue, maxExpenseTotalDailyCategoryValueTotal, barOrPieButtonIndex, itemsOrCategoriesButtonIndex, totalIncomesDailyValue ->
                CombinedFlow2(
                    totalExpensesDailyValue,
                    maxExpenseTotalDailyCategoryValueTotal,
                    barOrPieButtonIndex,
                    itemsOrCategoriesButtonIndex,
                    totalIncomesDailyValue
                )
            }
        ) {
            combined1, combined2 ->//maxExpenseCategoryValueTotal, allExpensesTotal, barOrPieButtonIndex, itemsOrCategoriesButtonIndex ->
            HomeUiState(
                expenseList = combined1.expenseList,
                incomeList = combined1.incomeList,
                expenseCategoryList = combined1.expenseCategoryList,
                incomeCategoryList = combined1.incomeCategoryList,
                maxExpenseDailyValue = combined1.maxExpenseDailyValue,
                maxTotalDailyCategoryValue = combined2.maxExpenseDailyCategoryValueTotal,
                totalExpensesDailyValue = combined2.allExpensesDailyTotal,
                barOrPie = combined2.barOrPieButtonIndex,
                itemsOrCategories = combined2.itemsOrCategoriesButtonIndex,
                totalIncomesDailyValue = combined2.totalIncomesDailyValue
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
    val expenseList: List<BudgetItem>,
    val incomeList: List<BudgetItem>,
    val expenseCategoryList: List<Category>,
    val incomeCategoryList: List<Category>,
    val maxExpenseDailyValue: Double,
)
data class CombinedFlow2(
    val allExpensesDailyTotal: Double,
    val maxExpenseDailyCategoryValueTotal: Double,
    val barOrPieButtonIndex: Int,
    val itemsOrCategoriesButtonIndex: Int,
    val totalIncomesDailyValue: Double
)


data class HomeUiState(
    val expenseList: List<BudgetItem> = listOf(),
    val incomeList: List<BudgetItem> = listOf(),
    val expenseCategoryList: List<Category> = listOf(),
    val incomeCategoryList: List<Category> = listOf(),
    val maxExpenseDailyValue: Double = 0.0,
    val maxTotalDailyCategoryValue: Double = 0.0,
    val totalExpensesDailyValue: Double = 0.0,
    val barOrPie: Int = 0,
    val itemsOrCategories: Int = 0,
    val totalIncomesDailyValue: Double = 0.0
)
