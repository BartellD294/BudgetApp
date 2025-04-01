package com.example.budgetapp2.data

import kotlinx.coroutines.flow.Flow

interface BudgetItemsRepository {
    fun getAllItemsStream(): Flow<List<BudgetItem>>
    fun getAllCategoriesStream() : Flow<List<Category>>

    fun getCategoryCost(category: String): Flow<Double>

    fun getMaxAmount(): Flow<Double>
    fun getMaxCostPerWeek(): Flow<Double>
    fun getMaxCost(): Flow<Double>
    fun getMaxCategoryTotal(): Flow<Double>
    fun getItemById(id: Int): Flow<BudgetItem>
    fun getExpensesWithApiKey(): Flow<List<BudgetItem>>
    fun getAllExpensesTotal(): Flow<Double>
    suspend fun insertExpense(expense: BudgetItem)
    suspend fun deleteExpense(expense: BudgetItem)
    suspend fun updateExpense(expense: BudgetItem)
    suspend fun updateItemsWithApiIds()
}