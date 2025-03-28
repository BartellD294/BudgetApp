package com.example.budgetapp2.data

import kotlinx.coroutines.flow.Flow

interface BudgetItemsRepository {
    fun getAllItemsStream(): Flow<List<BudgetItem>>

    fun getMaxAmount(): Flow<Double>
    fun getMaxCostPerWeek(): Flow<Double>
    fun getMaxCost(): Flow<Double>
    fun getItemById(id: Int): Flow<BudgetItem>
    fun getExpensesWithApiKey(): Flow<List<BudgetItem>>
    suspend fun insertExpense(expense: BudgetItem)
    suspend fun deleteExpense(expense: BudgetItem)
    suspend fun updateExpense(expense: BudgetItem)
    suspend fun updateItemsWithApiIds()
}