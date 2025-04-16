package com.example.budgetapp2.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: BudgetItem)
    @Update
    suspend fun updateItem(item: BudgetItem)
    @Delete
    suspend fun deleteItem(item: BudgetItem)

    @Query("SELECT * FROM expenses")
    fun getAllItems(): Flow<List<BudgetItem>>
    @Query("SELECT * FROM expenses WHERE expenseOrIncome = :expenseOrIncome")
    fun getAllExpensesOrIncomes(expenseOrIncome: Int): Flow<List<BudgetItem>>

    @Query("SELECT * FROM expenses WHERE id = :id")
    fun getItemById(id: Int): Flow<BudgetItem>
    @Query("SELECT DISTINCT category FROM expenses")
    fun getAllCategoryNames(): Flow<List<String>>
    @Query("SELECT DISTINCT category FROM expenses WHERE expenseOrIncome = :expenseOrIncome")
    fun getAllExpenseOrIncomeCategoryNames(expenseOrIncome: Int): Flow<List<String>>
    @Query("SELECT DISTINCT subcategory FROM expenses WHERE category = :category AND subcategory IS NOT NULL")
    fun getAllNotNullSubcategoryNamesOfCategory(category: String): Flow<List<String>>

    @Query("SELECT * FROM expenses WHERE category = :category")
    fun getAllItemsOfCategory(category: String): Flow<List<BudgetItem>>
    @Query("SELECT * FROM expenses WHERE category = :category AND expenseOrIncome = :expenseOrIncome")
    fun getAllExpensesOrIncomesOfCategory(category: String, expenseOrIncome: Int): Flow<List<BudgetItem>>
    @Query("SELECT * FROM expenses WHERE category IS NULL")
    fun getAllItemsWithoutCategory(): Flow<List<BudgetItem>>
    @Query("SELECT * FROM expenses WHERE category IS NULL AND expenseOrIncome = :expenseOrIncome")
    fun getAllExpensesOrIncomesWithoutCategory(expenseOrIncome: Int): Flow<List<BudgetItem>>

    @Query("SELECT SUM (value) FROM expenses WHERE expenseOrIncome = :expenseOrIncome")
    fun getTotalValueExpensesOrIncomes(expenseOrIncome: Int): Flow<Double>
    @Query("SELECT SUM (valuePerDay) FROM expenses WHERE expenseOrIncome = :expenseOrIncome")
    fun getTotalDailyValueExpensesOrIncomes(expenseOrIncome: Int): Flow<Double>
    @Query("SELECT MAX (value) FROM expenses WHERE expenseOrIncome = :expenseOrIncome")
    fun getMaxValueExpensesOrIncomes(expenseOrIncome: Int): Flow<Double>
    @Query("SELECT MAX (valuePerDay) FROM expenses WHERE expenseOrIncome = :expenseOrIncome")
    fun getMaxDailyValueExpensesOrIncomes(expenseOrIncome: Int): Flow<Double>
    @Query("SELECT SUM (value) FROM expenses WHERE category = :category AND expenseOrIncome = :expenseOrIncome")
    fun getTotalCategoryValueExpensesOrIncomes(category: String, expenseOrIncome: Int): Flow<Double>
    @Query("SELECT SUM (valuePerDay) FROM expenses WHERE category = :category AND expenseOrIncome = :expenseOrIncome")
    fun getTotalDailyCategoryValueExpensesOrIncomes(category: String, expenseOrIncome: Int): Flow<Double>


    //no income counterpart because incomes don't have quantity
    @Query("SELECT MAX (quantity) FROM expenses WHERE expenseOrIncome = 1")
    fun getMaxExpenseQuantity(): Flow<Double>

    @Query("SELECT * FROM expenses WHERE seriesId IS NOT NULL")
    fun getAllExpensesWithApiIds(): Flow<List<BudgetItem>>

    //@Query("SELECT MAX (amountPerWeek) FROM expenses")
    //fun getMaxCostPerWeek(): Flow<Double>
}