package com.example.budgetapp2.data

import android.util.Log
import com.example.budgetapp2.network.BudgetApiService
import kotlinx.coroutines.flow.Flow

class OfflineBudgetItemsRepository(
    private val budgetItemDao: BudgetItemDao,
    private val apiService: BudgetApiService
) : BudgetItemsRepository {

    private val apiKey = "884bbca41956e979caa1e58636c77461"

    override fun getAllItemsStream(): Flow<List<BudgetItem>> = budgetItemDao.getAllExpenses()
    override fun getMaxAmount(): Flow<Double> = budgetItemDao.getMaxAmount()
    override fun getMaxCostPerWeek(): Flow<Double> = budgetItemDao.getMaxCostPerWeek()
    override fun getMaxCost(): Flow<Double> = budgetItemDao.getMaxCost()
    override fun getItemById(id: Int): Flow<BudgetItem> = budgetItemDao.getItemById(id)
    override fun getExpensesWithApiKey(): Flow<List<BudgetItem>> = budgetItemDao.getExpensesWithApiKey()
    override suspend fun insertExpense(expense: BudgetItem) = budgetItemDao.insertExpense(expense)
    override suspend fun deleteExpense(expense: BudgetItem) = budgetItemDao.deleteExpense(expense)
    override suspend fun updateExpense(expense: BudgetItem) = budgetItemDao.updateExpense(expense)
    override suspend fun updateItemsWithApiKeys() {
        budgetItemDao.getExpensesWithApiKey().collect { items ->
            Log.i("length", items.size.toString())
            for (item in items) {
                item.apiKey.let { series ->
                    try {
                        val apiResponse = apiService.getLatestCost(series!!, apiKey)
                        if (apiResponse.observ.isNotEmpty()) {
                            Log.i ("updateItemsWithApiKeys",
                                apiResponse.toString()
                            )
                            val latestCost = apiResponse.observ[0].cost
                            val updatedItem = item.copy(cost = latestCost.toDouble())
                            budgetItemDao.updateExpense(updatedItem)


                        } else {

                        }
                    } catch (e: Exception) {
                        Log.e("updateItemsWithApiKeys", e.message.toString())
                    }
                }
            }
        }
    }
}