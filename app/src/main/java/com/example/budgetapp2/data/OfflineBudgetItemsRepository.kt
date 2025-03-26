package com.example.budgetapp2.data

import android.util.Log
import com.example.budgetapp2.network.BudgetApiInterface
import kotlinx.coroutines.flow.Flow

class OfflineBudgetItemsRepository(
    private val budgetItemDao: BudgetItemDao,
    private val apiInterface: BudgetApiInterface
) : BudgetItemsRepository {

    private val apiKey = "884bbca41956e979caa1e58636c77461"

    override fun getAllItemsStream(): Flow<List<BudgetItem>> = budgetItemDao.getAllExpenses()
    override fun getItemsStream(id: Int): Flow<BudgetItem?> = budgetItemDao.getExpenseById(id)
    override fun getMaxAmount(): Flow<Double> = budgetItemDao.getMaxAmount()
    override fun getMaxCostPerWeek(): Flow<Double> = budgetItemDao.getMaxCostPerWeek()
    override fun getMaxCost(): Flow<Double> = budgetItemDao.getMaxCost()
    override fun getExpensesWithApiKey(): Flow<List<BudgetItem>> = budgetItemDao.getExpensesWithApiKey()
    override suspend fun insertExpense(expense: BudgetItem) = budgetItemDao.insertExpense(expense)
    override suspend fun deleteExpense(expense: BudgetItem) = budgetItemDao.deleteExpense(expense)
    override suspend fun updateExpense(expense: BudgetItem) = budgetItemDao.updateExpense(expense)
    override suspend fun updateItemsWithApiKeys() {
        budgetItemDao.getExpensesWithApiKey().collect { items ->
            for (item in items) {
                item.apiKey.let { series ->
                    //try {
                        //val apiResponse = apiInterface.getCost(series!!, apiKey)
                    val apiResponse = apiInterface.getGNPCA()
                        //if (apiResponse.isSuccessful) {
                            Log.i(
                                "updateItemsWithApiKeys",
                                apiResponse.body()!!.observations.firstOrNull()!!.value.toString())
                        //}
                    //} catch (e: Exception) {
                      //  Log.e("updateItemsWithApiKeys", e.message.toString())
                    //}
                }
            }
        }

    }
}