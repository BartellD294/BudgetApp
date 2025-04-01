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
    suspend fun insertExpense(expense: BudgetItem)

    @Update
    suspend fun updateExpense(expense: BudgetItem)


    @Delete
    suspend fun deleteExpense(expense: BudgetItem)

    @Query("SELECT MAX (amount) FROM expenses")
    fun getMaxAmount(): Flow<Double>

    @Query("SELECT MAX (cost) FROM expenses")
    fun getMaxCost(): Flow<Double>

    @Query("SELECT SUM (cost) FROM expenses WHERE category = :category")
    fun getCategoryCost(category: String): Flow<Double>

    @Query("SELECT MAX (amountPerWeek) FROM expenses")
    fun getMaxCostPerWeek(): Flow<Double>

    @Query("SELECT * FROM expenses")
    fun getAllExpenses(): Flow<List<BudgetItem>>

    @Query("SELECT SUM (cost) FROM expenses")
    fun getAllExpensesTotal(): Flow<Double>

    @Query("SELECT DISTINCT category FROM expenses")
    fun getAllCategories(): Flow<List<String>>

    @Query("SELECT * FROM expenses WHERE id = :id")
    fun getItemById(id: Int): Flow<BudgetItem>

    @Query("SELECT * FROM expenses WHERE category = :category")
    fun getExpensesByCategory(category: String): Flow<List<BudgetItem>>

    @Query("SELECT * FROM expenses WHERE seriesId IS NOT NULL")
    fun getExpensesWithApiIds(): Flow<List<BudgetItem>>
}