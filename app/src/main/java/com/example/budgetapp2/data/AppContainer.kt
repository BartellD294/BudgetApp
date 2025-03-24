package com.example.budgetapp2.data

import android.content.Context
import android.util.Log
import androidx.room.Room
import java.io.File

/*
interface AppContainer {
    var repository: BudgetItemsRepository?
}

*/
class AppDataContainer(
    private val context: Context,
    private val file: File? = null
) {
    var database: BudgetDatabase? = BudgetDatabase.getDatabase(context)
    /* override */ var repository: BudgetItemsRepository = OfflineBudgetItemsRepository(database!!.budgetItemDao())

    fun getDatabase(context: Context): BudgetDatabase {
        return database ?: synchronized(this) {
            database ?: createDatabase(context).also {database = it}
        }
    }

    fun createDatabase(context: Context): BudgetDatabase {
        return Room.databaseBuilder(
            context,
            BudgetDatabase::class.java,
            "budget_item_database"
        ).build()
    }

    fun updateDatabase(file: File) {

        Log.i("update database", "update database")
        database = BudgetDatabase.getDatabase(context, file)
        repository = OfflineBudgetItemsRepository(database!!.budgetItemDao())

    }

    fun updateRepository() {
        repository = OfflineBudgetItemsRepository(database!!.budgetItemDao())
    }

    fun resetDatabase(context: Context) {
        database!!.close()
        database = createDatabase(context)

    }
}