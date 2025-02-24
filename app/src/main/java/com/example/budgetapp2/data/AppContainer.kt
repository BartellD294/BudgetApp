package com.example.budgetapp2.data

import android.content.Context

class AppContainer(private val context: Context) {
    val itemsRepository: BudgetItemsRepository by lazy {
        OfflineBudgetItemsRepository(BudgetDatabase.getDatabase(context).budgetItemDao())
    }
}