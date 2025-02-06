package com.example.budgetapp2.data

import kotlinx.coroutines.flow.Flow

interface BudgetItemsRepository {
    fun getAllItemsStream(): Flow<List<BudgetItem>>

    fun getItemsStream(id: Int): Flow<BudgetItem?>
    suspend fun insertExpense(expense: BudgetItem)
    suspend fun deleteExpense(expense: BudgetItem)
    suspend fun updateExpense(expense: BudgetItem)
}