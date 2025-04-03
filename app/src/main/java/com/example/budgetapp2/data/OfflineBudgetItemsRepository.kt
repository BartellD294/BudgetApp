package com.example.budgetapp2.data

import android.util.Log
import com.example.budgetapp2.network.BudgetApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class OfflineBudgetItemsRepository(
    private val budgetItemDao: BudgetItemDao,
    private val apiService: BudgetApiService
) : BudgetItemsRepository {

    private val apiKey = "884bbca41956e979caa1e58636c77461"

    override fun getAllItemsStream(): Flow<List<BudgetItem>> = budgetItemDao.getAllExpenses()
    override fun getAllCategoriesStream(): Flow<List<Category>> {
        return budgetItemDao.getAllCategories().map { categoryList ->
            categoryList.map { category ->
                Category(category, budgetItemDao.getCategoryCost(category).first())
            }
        }
    }
    override fun getAllCategoryNames(): Flow<List<String>> = budgetItemDao.getAllCategories()

    //override fun getAllItemsByCategory(): Flow<List<List<BudgetItem>>> = budgetItemDao.getAllItemsByCategory()

    override fun getItemsByCategory(category: String): Flow<List<BudgetItem>> = budgetItemDao.getExpensesByCategory(category)

    override fun getAllItemsWithoutCategories(): Flow<List<BudgetItem>> = budgetItemDao.getAllItemsWithoutCategories()
    override fun getAllExpensesTotal(): Flow<Double> = budgetItemDao.getAllExpensesTotal()
    override fun getMaxAmount(): Flow<Double> = budgetItemDao.getMaxAmount()
    override fun getMaxCostPerWeek(): Flow<Double> = budgetItemDao.getMaxCostPerWeek()
    override fun getMaxCost(): Flow<Double> = budgetItemDao.getMaxCost()
    override fun getMaxCategoryTotal(): Flow<Double> =
        budgetItemDao.getAllCategories().map { categoryList ->
            categoryList.map { category ->
                budgetItemDao.getCategoryCost(category).first()
            }.maxOrNull() ?: 0.0
        }

    override fun getCategoryCost(category: String): Flow<Double> = budgetItemDao.getCategoryCost(category)
    override fun getItemById(id: Int): Flow<BudgetItem> = budgetItemDao.getItemById(id)
    override fun getExpensesWithApiKey(): Flow<List<BudgetItem>> = budgetItemDao.getExpensesWithApiIds()
    override suspend fun insertExpense(expense: BudgetItem) = budgetItemDao.insertExpense(expense)
    override suspend fun deleteExpense(expense: BudgetItem) = budgetItemDao.deleteExpense(expense)
    override suspend fun updateExpense(expense: BudgetItem) = budgetItemDao.updateExpense(expense)
    override suspend fun updateItemsWithApiIds() {
        budgetItemDao.getExpensesWithApiIds().first().forEach { item ->
            try {
                val apiResponse = apiService.getLatestCost(item.seriesId!!, apiKey)
                if (apiResponse.observ.isNotEmpty()) {
                    Log.i(
                        "updateItemsWithApiKeys",
                        apiResponse.toString()
                    )
                    val latestCost = apiResponse.observ[0].cost
                    val updatedItem = item.copy(cost = latestCost.toDouble())
                    budgetItemDao.updateExpense(updatedItem)
                }
            } catch (e: Exception) {
                Log.e("updateItemsWithApiKeys", e.message.toString())
            }
        }
    }
}

data class Category(val name: String, val totalCost: Double)