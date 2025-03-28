package com.example.budgetapp2.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.budgetapp2.BudgetApplication
import com.example.budgetapp2.data.AppDataContainer
import com.example.budgetapp2.data.BudgetItem
import com.example.budgetapp2.data.BudgetItemsRepository
import kotlinx.coroutines.flow.first

class AddItemViewModel(private val application: BudgetApplication): ViewModel() {

    var expenseUiState by mutableStateOf(ExpenseUiState())
        private set

    fun updateUiState(newExpenseUiState: ExpenseUiState) {
        this.expenseUiState = newExpenseUiState
    }

    suspend fun updateUiStateById(id: Int) {
        val budgetItem = application.container.repository.getItemById(id).first()
        this.expenseUiState = budgetItem.toExpenseUiState()
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
    fun switchUseApiKey() {
        if (expenseUiState.useApiKey) {
            expenseUiState = expenseUiState.copy(apiKey = "")
        }
        expenseUiState = expenseUiState.copy(useApiKey = !expenseUiState.useApiKey)
    }

    fun updateAmount(amount: String) {
        expenseUiState = expenseUiState.copy(amount = amount)
    }

    fun updateApiKey(apiKey: String) {
        expenseUiState = expenseUiState.copy(apiKey = apiKey)
    }

    suspend fun enterExpense() {
        Log.i("Entering item", expenseUiState.toString())
        val newExpense = expenseUiState.toExpense()
        application.container.repository.insertExpense(newExpense)
    }
}

data class ExpenseUiState(
    val buttonIndex: Int = 0,
    val id: Int = 0,
    val name: String = "",
    val category: String = "",
    val cost: String = "",
    val amount: String = "",
    val frequency: Int = 0,
    val useApiKey: Boolean = false,
    val apiKey: String = ""
)

fun ExpenseUiState.toExpense(): BudgetItem {
    val itemApiKey: String?
    if (apiKey == "") {
        itemApiKey = null
    } else {
        itemApiKey = apiKey
    }
    return BudgetItem(
        id = id,
        name = name,
        cost = cost.toDouble(),
        amount = amount.toDouble(),
        frequency = frequency.toDouble(),
        category = category,
        date = "",
        seriesId = itemApiKey
    )
}

fun BudgetItem.toExpenseUiState(buttonIndex: Int = 0): ExpenseUiState = ExpenseUiState(
    buttonIndex = buttonIndex,
    id = id,
    name = name,
    cost = amount.toString(),
    amount = amount.toString(),
    frequency = frequency.toInt(),
    category = category,
    useApiKey = seriesId != null,
    apiKey = seriesId ?: ""

)