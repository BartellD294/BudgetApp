package com.example.budgetapp2.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.budgetapp2.data.BudgetItem
import com.example.budgetapp2.data.BudgetItemsRepository
import kotlinx.coroutines.flow.first

class AddItemViewModel(private val budgetItemsRepository: BudgetItemsRepository): ViewModel() {

    var expenseUiState by mutableStateOf(ExpenseUiState())
        private set

    fun updateUiState(newExpenseUiState: ExpenseUiState) {
        this.expenseUiState = newExpenseUiState
    }

    suspend fun updateUiStateById(id: Int) {
        val budgetItem = budgetItemsRepository.getItemsStream(id).first()
        this.expenseUiState = budgetItem!!.toExpenseUiState()
    }

    fun updateName(name: String) {
        expenseUiState = expenseUiState.copy(name = name)
    }

    fun updateCategory(category: String) {
        expenseUiState = expenseUiState.copy(category = category)
    }

    fun updateCost(cost: String) {
        expenseUiState = expenseUiState.copy(cost = cost)
    }

    fun updateFrequency(frequency: Int) {
        expenseUiState = expenseUiState.copy(frequency = frequency)
    }

    fun updateButton(buttonIndex: Int) {
        expenseUiState = expenseUiState.copy(buttonIndex = buttonIndex)
    }

    suspend fun enterExpense() {
        val newExpense = expenseUiState.toExpense()

        budgetItemsRepository.insertExpense(newExpense)
    }
}

data class ExpenseUiState(
    val buttonIndex: Int = 0,
    val id: Int = 0,
    val name: String = "",
    val category: String = "",
    val cost: String = "",
    val frequency: Int = 0
)

fun ExpenseUiState.toExpense(): BudgetItem = BudgetItem(
    id = id,
    name = name,
    category = category,
    amount = (cost.toDoubleOrNull()) ?: 0.0,
    frequency = frequency,
    date = ""
)

fun BudgetItem.toExpenseUiState(buttonIndex: Int = 0): ExpenseUiState = ExpenseUiState(
    buttonIndex = buttonIndex,
    id = id,
    name = name,
    category = category,
    cost = amount.toString(),
)