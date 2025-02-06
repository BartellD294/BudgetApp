package com.example.budgetapp2.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities= [BudgetItem::class], version=1, exportSchema = false)
abstract class BudgetDatabase : RoomDatabase() {
    abstract fun budgetItemDao(): BudgetItemDao
    companion object {
        @Volatile
        private var Instance: BudgetDatabase? = null
        fun getDatabase(context: Context): BudgetDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, BudgetDatabase::class.java, "budget_item_database")
                    .build()
                    .also { Instance = it }}
            }
    }

}