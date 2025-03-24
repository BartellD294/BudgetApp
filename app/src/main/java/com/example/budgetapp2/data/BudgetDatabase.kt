package com.example.budgetapp2.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

@Database(entities= [BudgetItem::class], version=1, exportSchema = false)
abstract class BudgetDatabase : RoomDatabase() {

    abstract fun budgetItemDao(): BudgetItemDao

    companion object {
        @Volatile
        private var Instance: BudgetDatabase? = null

        fun getDatabase(context: Context, file: File? = null): BudgetDatabase {
            if (file == null) {
                return Instance ?: synchronized(this) {
                    Room.databaseBuilder(
                        context,
                        BudgetDatabase::class.java,
                        "budget_item_database"
                    )
                        .addCallback()
                        .fallbackToDestructiveMigration()
                        .build()
                        .also { Instance = it }
                }
            } else {
                return Instance ?: synchronized(this) {
                    Room.databaseBuilder(
                        context,
                        BudgetDatabase::class.java,
                        "budget_item_database"
                    )
                        .createFromFile(file)
                        .build()
                        .also { Instance = it }
                }
            }
        }
    }
}
