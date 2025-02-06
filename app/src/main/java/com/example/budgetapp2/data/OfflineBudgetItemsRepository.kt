package com.example.budgetapp2.data

import kotlinx.coroutines.flow.Flow

class OfflineBudgetItemsRepository(private val budgetItemDao: BudgetItemDao) : BudgetItemsRepository {
    override fun getAllItemsStream(): Flow<List<BudgetItem>> = budgetItemDao.getAllExpenses()
    override fun getItemsStream(id: Int): Flow<BudgetItem?> = budgetItemDao.getExpenseById(id)
    override suspend fun insertExpense(expense: BudgetItem) = budgetItemDao.insertExpense(expense)
    override suspend fun deleteExpense(expense: BudgetItem) = budgetItemDao.deleteExpense(expense)
    override suspend fun updateExpense(expense: BudgetItem) = budgetItemDao.updateExpense(expense)
}