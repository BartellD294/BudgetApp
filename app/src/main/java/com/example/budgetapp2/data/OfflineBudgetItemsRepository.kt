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

    override suspend fun insertItem(item: BudgetItem) =
        budgetItemDao.insertItem(item)

    override suspend fun updateItem(item: BudgetItem) =
        budgetItemDao.updateItem(item)

    override suspend fun deleteItem(item: BudgetItem) =
        budgetItemDao.deleteItem(item)

    override fun getAllItems(): Flow<List<BudgetItem>> =
        budgetItemDao.getAllItems()

    override fun getAllExpensesOrIncomes(expenseOrIncome: Int): Flow<List<BudgetItem>> =
        budgetItemDao.getAllExpensesOrIncomes(expenseOrIncome)

    override fun getItemById(id: Int): Flow<BudgetItem> =
        budgetItemDao.getItemById(id)

    override fun getAllCategoryNames(): Flow<List<String>> =
        budgetItemDao.getAllCategoryNames()

    override fun getAllExpenseOrIncomeCategoryNames(expenseOrIncome: Int): Flow<List<String>> =
        budgetItemDao.getAllExpenseOrIncomeCategoryNames(expenseOrIncome)

    override fun getAllNotNullSubcategoryNamesOfCategory(category: String): Flow<List<String>> =
        budgetItemDao.getAllNotNullSubcategoryNamesOfCategory(category)

    override fun getAllItemsOfCategory(category: String): Flow<List<BudgetItem>> =
        budgetItemDao.getAllItemsOfCategory(category)

    override fun getAllExpensesOrIncomesOfCategory(
        category: String,
        expenseOrIncome: Int
    ): Flow<List<BudgetItem>> =
        budgetItemDao.getAllExpensesOrIncomesOfCategory(category, expenseOrIncome)

    override fun getAllItemsWithoutCategory(): Flow<List<BudgetItem>> =
        budgetItemDao.getAllItemsWithoutCategory()

    override fun getAllExpensesOrIncomesWithoutCategory(expenseOrIncome: Int): Flow<List<BudgetItem>> =
        budgetItemDao.getAllExpensesOrIncomesWithoutCategory(expenseOrIncome)

    override fun getTotalValueExpensesOrIncomes(expenseOrIncome: Int): Flow<Double> =
        budgetItemDao.getTotalValueExpensesOrIncomes(expenseOrIncome)

    override fun getMaxValueExpensesOrIncomes(expenseOrIncome: Int): Flow<Double> =
        budgetItemDao.getMaxValueExpensesOrIncomes(expenseOrIncome)

    override fun getTotalCategoryValueExpensesOrIncomes(
        category: String,
        expenseOrIncome: Int
    ): Flow<Double> =
        budgetItemDao.getTotalCategoryValueExpensesOrIncomes(category, expenseOrIncome)

    override fun getMaxExpenseQuantity(): Flow<Double> =
        budgetItemDao.getMaxExpenseQuantity()

    override fun getAllExpensesWithApiIds(): Flow<List<BudgetItem>> =
        budgetItemDao.getAllExpensesWithApiIds()

    override fun getMaxTotalCategoryValueExpenseOrIncome(expenseOrIncome: Int): Flow<Double> =
        budgetItemDao.getAllExpenseOrIncomeCategoryNames(expenseOrIncome).map { categoryList ->
            categoryList.map { category ->
                budgetItemDao.getTotalCategoryValueExpensesOrIncomes(category, expenseOrIncome)
                    .first()
            }.maxOrNull() ?: 0.0
        }

    override suspend fun updateItemsWithApiIds() {
        budgetItemDao.getAllExpensesWithApiIds().first().forEach { item ->
            try {
                val apiResponse = apiService.getLatestCost(item.seriesId!!, apiKey)
                if (apiResponse.observ.isNotEmpty()) {
                    Log.i(
                        "updateItemsWithApiKeys",
                        apiResponse.toString()
                    )
                    val latestCost = apiResponse.observ[0].cost
                    val updatedItem = item.copy(value = latestCost.toDouble())
                    budgetItemDao.updateItem(updatedItem)
                }
            } catch (e: Exception) {
                Log.e("updateItemsWithApiKeys", e.message.toString())
            }
        }
    }

    //override fun getMaxCostPerWeek(): Flow<Double> = budgetItemDao.getMaxCostPerWeek()

    override fun getAllCategoriesExpensesOrIncomes(expenseOrIncome: Int): Flow<List<Category>> {
        return budgetItemDao.getAllCategoryNames().map { categoryList ->
            categoryList.map { category ->
                val totalValue =
                    budgetItemDao.getTotalCategoryValueExpensesOrIncomes(category, expenseOrIncome)
                        .first()
                Category(category, totalValue, getAllSubcategoriesOfCategory(category).first()) // getAllSubcategoriesOfCategory(category).first())
            }
        }
    }

    override fun getAllSubcategoriesOfCategory(category: String): Flow<List<Subcategory>> {
        return budgetItemDao.getAllNotNullSubcategoryNamesOfCategory(category).map { subcategoryNames ->
            subcategoryNames.map { subcategoryName ->
                Subcategory(subcategoryName)
            }
        }
    }
}

data class Category(val name: String, val totalValue: Double, val subcategories: List<Subcategory> = listOf())

data class Subcategory(val name: String = "")