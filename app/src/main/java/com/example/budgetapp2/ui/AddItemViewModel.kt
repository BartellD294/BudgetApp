package com.example.budgetapp2.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.query
import com.example.budgetapp2.BudgetApplication
import com.example.budgetapp2.data.BudgetItem
import com.example.budgetapp2.data.Category
import com.example.budgetapp2.data.Subcategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


data class AddItemFlow1(
    val id: Int = 0,
    val buttonIndex: Int = 0,
    val name: String = "",
    val value: String = "",
    val quantity: String = "",
)

data class AddItemFlow2(
    val category: String = "",
    val subcategory: String = "",
    val frequency: Int = 0,
    val useApiKey: Boolean = false,
    val apiKey: String = ""
)

class AddItemViewModel(private val application: BudgetApplication): ViewModel() {

    private val id = MutableStateFlow(0)
    private val buttonIndex = MutableStateFlow(0)
    private val name = MutableStateFlow("")
    private val value = MutableStateFlow("")
    private val quantity = MutableStateFlow("")
    private val category = MutableStateFlow("")
    private val subcategory = MutableStateFlow("")
    private val frequency = MutableStateFlow(0)
    private val useApiKey = MutableStateFlow(false)
    private val apiKey = MutableStateFlow("")
    private val categoryList: MutableStateFlow<List<Category>> =
        MutableStateFlow(emptyList())
    var expenseUiState: StateFlow<ExpenseUiState> =
        combine(
            combine(
                id, buttonIndex, name, value, quantity
            ) { id, buttonIndex, name, value, quantity ->
                AddItemFlow1(
                    id, buttonIndex, name, value, quantity
                )
            },
            combine(
                category, subcategory, frequency, useApiKey, apiKey
            ) { category, subcategory, frequency, useApiKey, apiKey ->
                AddItemFlow2(
                    category, subcategory, frequency, useApiKey, apiKey
                )
            },
            categoryList
        ) { combined1, combined2, categoryList ->
            ExpenseUiState(
                id = combined1.id,
                buttonIndex = combined1.buttonIndex,
                name = combined1.name,
                value = combined1.value,
                quantity = combined1.quantity,
                category = combined2.category,
                subcategory = combined2.subcategory,
                frequency = combined2.frequency,
                useApiKey = combined2.useApiKey,
                apiKey = combined2.apiKey,
                categoryList = categoryList.map { it.name }
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ExpenseUiState()
        )

    // https://kotlinlang.org/docs/classes.html#constructors
    init {
        viewModelScope.launch {
            updateCategoryList(
                application.container.repository.getAllCategoriesExpensesOrIncomes(buttonIndex.value).first()
            )
        }
    }


    suspend fun updateUiStateById(id: Int) {
        val budgetItem = application.container.repository.getItemById(id).first()

        updateId(budgetItem.id)
        updateButton(budgetItem.expenseOrIncome)
        updateName(budgetItem.name)
        updateValue(budgetItem.value.toString())
        updateQuantity(budgetItem.quantity.toString())

        updateCategory(budgetItem.category.orEmpty())
        updateSubcategory(budgetItem.subcategory.orEmpty())
        updateFrequency(budgetItem.frequency.toInt())
        updateUseApiKey(budgetItem.seriesId != null)
        updateApiKey(budgetItem.seriesId ?: "")

        updateCategoryList(application.container.repository.getAllCategoriesExpensesOrIncomes(buttonIndex.value).first())
    }

    fun updateId(newId: Int)
        { id.value = newId}
    fun updateButton(newButtonIndex: Int)
        { buttonIndex.value = newButtonIndex }
    fun updateName(newName: String)
        { name.value = newName }
    fun updateValue(newValue: String)
        { value.value = newValue }
    fun updateQuantity(newQuantity: String)
        { quantity.value = newQuantity }
    fun updateCategory(newCategory: String)
        { category.value = newCategory }
    fun updateSubcategory(newSubcategory: String)
        { subcategory.value = newSubcategory }
    fun updateFrequency(newFrequency: Int)
        { frequency.value = newFrequency }
    fun updateUseApiKey(newUseApiKey: Boolean)
        { useApiKey.value = newUseApiKey }
    fun updateApiKey(newApiKey: String)
        { apiKey.value = newApiKey }

    fun updateCategoryList(newCategoryList: List<Category>)
        { categoryList.value = newCategoryList }

    fun switchUseApiKey() {
        if (useApiKey.value) { apiKey.value = "" }
        useApiKey.value = !useApiKey.value
    }
    suspend fun enterItem() {
        Log.i("Entering item", expenseUiState.toString())
        val newExpense = expenseUiState.value.toExpense()
        application.container.repository.insertItem(newExpense)
    }
}

data class ExpenseUiState(
    val id: Int = 0,
    val buttonIndex: Int = 0,
    val name: String = "",
    val value: String = "",
    val quantity: String = "",
    val category: String = "",
    val subcategory: String = "",
    val frequency: Int = 0,
    val useApiKey: Boolean = false,
    val apiKey: String = "",
    val categoryList: List<String> = listOf()
)

fun ExpenseUiState.toExpense(): BudgetItem {
    return BudgetItem(
        id = id,
        expenseOrIncome = buttonIndex,
        name = name,
        value = value.toDouble(),
        quantity = quantity.toDouble(),
        category = category.ifEmpty { null },
        subcategory = subcategory.ifEmpty { null },
        frequency = frequency.toDouble(),
        date = "",
        seriesId = apiKey.ifEmpty { null }
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