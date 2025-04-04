package com.example.budgetapp2.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.room.util.query
import com.example.budgetapp2.BudgetApplication
import com.example.budgetapp2.data.BudgetItem
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

    fun updateValue(value: String) {
        expenseUiState = expenseUiState.copy(value = value)
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

    fun updateQuantity(quantity: String) {
        expenseUiState = expenseUiState.copy(quantity = quantity)
    }

    fun updateApiKey(apiKey: String) {
        expenseUiState = expenseUiState.copy(apiKey = apiKey)
    }

    suspend fun enterItem() {
        Log.i("Entering item", expenseUiState.toString())
        val newExpense = expenseUiState.toExpense()
        application.container.repository.insertItem(newExpense)
    }
}

data class ExpenseUiState(
    val buttonIndex: Int = 0,
    val id: Int = 0,
    val name: String = "",
    val category: String = "",
    val value: String = "",
    val quantity: String = "",
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
        expenseOrIncome = buttonIndex,
        name = name,
        value = value.toDouble(),
        quantity = quantity.toDouble(),
        category = category.ifEmpty { null },
        subcategory = null,
        frequency = frequency.toDouble(),
        date = "",
        seriesId = itemApiKey
    )
}

fun BudgetItem.toExpenseUiState(buttonIndex: Int = 0): ExpenseUiState = ExpenseUiState(
    buttonIndex = buttonIndex,
    id = id,
    name = name,
    value = value.toString(),
    quantity = quantity.toString(),
    frequency = frequency.toInt(),
    //Note to self, String?.orEmpty = normal string if not null
    // or "" if null
    category = category.orEmpty(),
    useApiKey = seriesId != null,
    apiKey = seriesId ?: ""

)