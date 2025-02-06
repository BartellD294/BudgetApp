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
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExpense(expense: BudgetItem)

    @Update
    suspend fun updateExpense(expense: BudgetItem)

    @Delete
    suspend fun deleteExpense(expense: BudgetItem)

    @Query("SELECT * FROM expenses")
    fun getAllExpenses(): Flow<List<BudgetItem>>

    @Query("SELECT * FROM expenses WHERE id = :id")
    fun getExpenseById(id: Int): Flow<BudgetItem>

    @Query("SELECT * FROM expenses WHERE category = :category")
    fun getExpensesByCategory(category: String): Flow<List<BudgetItem>>

}