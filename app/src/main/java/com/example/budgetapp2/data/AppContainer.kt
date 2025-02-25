package com.example.budgetapp2.data

import android.content.Context


interface AppContainer {
    val repository: BudgetItemsRepository
}
class AppDataContainer(private val context: Context): AppContainer {
    override val repository: BudgetItemsRepository by lazy {
        OfflineBudgetItemsRepository(BudgetDatabase.getDatabase(context).budgetItemDao())
    }
}