package com.example.budgetapp2.data

import androidx.room.Query
import kotlinx.coroutines.flow.Flow

interface BudgetItemsRepository {
    suspend fun insertItem(item: BudgetItem)
    suspend fun updateItem(item: BudgetItem)
    suspend fun deleteItem(item: BudgetItem)

    fun getAllItems(): Flow<List<BudgetItem>>
    fun getAllExpensesOrIncomes(expenseOrIncome: Int): Flow<List<BudgetItem>>

    fun getItemById(id: Int): Flow<BudgetItem>
    fun getAllCategoryNames(): Flow<List<String>>
    fun getAllExpenseOrIncomeCategoryNames(expenseOrIncome: Int): Flow<List<String>>
    fun getAllNotNullSubcategoryNamesOfCategory(category: String): Flow<List<String>>

    fun getAllItemsOfCategory(category: String): Flow<List<BudgetItem>>
    fun getAllExpensesOrIncomesOfCategory(category: String, expenseOrIncome: Int): Flow<List<BudgetItem>>
    fun getAllItemsWithoutCategory(): Flow<List<BudgetItem>>
    fun getAllExpensesOrIncomesWithoutCategory(expenseOrIncome: Int): Flow<List<BudgetItem>>

    fun getTotalValueExpensesOrIncomes(expenseOrIncome: Int): Flow<Double>
    fun getTotalDailyValueExpensesOrIncomes(expenseOrIncome: Int): Flow<Double>
    fun getMaxValueExpensesOrIncomes(expenseOrIncome: Int): Flow<Double>
    fun getMaxDailyValueExpensesOrIncomes(expenseOrIncome: Int): Flow<Double>
    fun getTotalCategoryValueExpensesOrIncomes(category: String, expenseOrIncome: Int): Flow<Double>
    fun getTotalDailyCategoryValueExpensesOrIncomes(category: String, expenseOrIncome: Int): Flow<Double>

    //no income counterpart because incomes don't have quantity
    fun getMaxExpenseQuantity(): Flow<Double>

    fun getAllExpensesWithApiIds(): Flow<List<BudgetItem>>

    //These are only in OfflineBudgetItemsRepository (not direct DAO calls)
    fun getAllCategoriesExpensesOrIncomes(expenseOrIncome: Int): Flow<List<Category>>
    fun getAllSubcategoriesOfCategory(category: String): Flow<List<Subcategory>>

    fun getMaxTotalCategoryValueExpenseOrIncome(expenseOrIncome: Int): Flow<Double>
    fun getMaxTotalDailyCategoryValueExpenseOrIncome(expenseOrIncome: Int): Flow<Double>
    suspend fun updateItemsWithApiIds()
}