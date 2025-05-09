package com.example.budgetapp2.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val frequency = MutableStateFlow(1)
    private val useApiKey = MutableStateFlow(false)
    private val apiKey = MutableStateFlow("")
    private val categoryList: MutableStateFlow<List<Category>> =
        MutableStateFlow(emptyList())
    private val subcategoryList: MutableStateFlow<List<Subcategory>> =
        MutableStateFlow(emptyList())
    private val enterOrUpdate = MutableStateFlow(0)

    var expenseUiState: StateFlow<ExpenseUiState> =
        combine(
            combine(
                id,
                buttonIndex,
                name,
                value,
                quantity
            ) { id,
                buttonIndex,
                name,
                value,
                quantity ->
                AddItemFlow1(
                    id,
                    buttonIndex,
                    name,
                    value,
                    quantity
                )
            },
            combine(
                category, subcategory, frequency, useApiKey, apiKey
            ) { category, subcategory, frequency, useApiKey, apiKey ->
                AddItemFlow2(
                    category, subcategory, frequency, useApiKey, apiKey
                )
            },
            categoryList, subcategoryList, enterOrUpdate
        ) { combined1, combined2, categoryList, subcategoryList, enterOrUpdate ->
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
                categoryList = categoryList,
                subcategoryList = subcategoryList,
                enterOrUpdate = enterOrUpdate
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
            updateSubcategoryList(category.value)
        }
    }


    suspend fun updateUiStateById(id: Int) {
        val budgetItem = application.container.repository.getItemById(id).first()

        updateId(budgetItem.id)
        updateButton(budgetItem.expenseOrIncome)
        updateName(budgetItem.name)
        updateValue(budgetItem.value.toString())
        updateQuantity(budgetItem.quantity.toString())

        updateCategory(budgetItem.category)//.orEmpty())
        updateSubcategory(budgetItem.subcategory)//.orEmpty())
        updateFrequency(budgetItem.frequency.toInt())
        updateUseApiKey(budgetItem.seriesId != null)
        updateApiKey(budgetItem.seriesId ?: "")

        updateCategoryList(application.container.repository.getAllCategoriesExpensesOrIncomes(buttonIndex.value).first())
        updateSubcategoryList(category.value)
    }

    private fun updateId(newId: Int)
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
        { category.value = newCategory
    updateSubcategoryList(category.value)}
    fun updateSubcategory(newSubcategory: String)
        { subcategory.value = newSubcategory }
    fun updateFrequency(newFrequency: Int)
        { frequency.value = newFrequency }
    private fun updateUseApiKey(newUseApiKey: Boolean)
        { useApiKey.value = newUseApiKey }
    fun updateApiKey(newApiKey: String)
        { apiKey.value = newApiKey }

    private fun updateCategoryList(newCategoryList: List<Category>)
        { categoryList.value = newCategoryList
            updateSubcategoryList(category.value)
    }

    private fun updateSubcategoryList(category: String) {
        val newSubcategoryList = mutableListOf<Subcategory>()
        for (i in categoryList.value.indices) {
            if (categoryList.value[i].name == category) {
                newSubcategoryList.addAll(categoryList.value[i].subcategories)
            }
        }
        subcategoryList.value = newSubcategoryList
        Log.i("Subcategory List", subcategoryList.value.toString())
    }

    fun switchUseApiKey() {
        if (useApiKey.value) { apiKey.value = "" }
        useApiKey.value = !useApiKey.value
    }
    suspend fun enterItem() {
        Log.i("Entering item", expenseUiState.toString())
        val newExpense = expenseUiState.value.toExpense()
        application.container.repository.insertItem(newExpense)
    }

    fun updateEnterOrUpdate(newValue: Int) {
        enterOrUpdate.value = newValue
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
    val categoryList: List<Category> = listOf(),
    val subcategoryList: List<Subcategory> = listOf(),
    val enterOrUpdate: Int = 0
)

fun ExpenseUiState.toExpense(): BudgetItem {
    return BudgetItem(
        id = id,
        expenseOrIncome = buttonIndex,
        name = name,
        value = if (value == "") {
            0.0
        } else {
            value.toDouble()
        },
        quantity = if (quantity == "") {
            null
        } else {
            quantity.toDouble()
        },
        category = category.ifEmpty { "Uncategorized" },
        subcategory = subcategory.ifEmpty { "No subcategory" },
        frequency = frequency.toDouble(),
        valuePerDay = if (buttonIndex == 0) { //expense
            value.toDouble() * quantity.toDouble() / frequency.toDouble()

        } else { // income
            value.toDouble() / frequency.toDouble()
        },
        seriesId = apiKey.ifEmpty { null }
    )
}

//fun BudgetItem.toExpenseUiState(buttonIndex: Int = 0): ExpenseUiState = ExpenseUiState(
//    buttonIndex = buttonIndex,
//    id = id,
//    name = name,
//    value = value.toString(),
//    quantity = quantity.toString(),
//    frequency = frequency.toInt(),
//    //Note to self, String?.orEmpty = normal string if not null
//    // or "" if null
//    category = category.orEmpty(),
//    useApiKey = seriesId != null,
//    apiKey = seriesId ?: ""
//
//)