package com.example.budgetapp2.data

import android.content.Context
import java.io.File


interface AppContainer {
    val repository: BudgetItemsRepository
}
class AppDataContainer(
    private val context: Context,
    private val file: File? = null
): AppContainer {
    override val repository: BudgetItemsRepository by lazy {
        if (file == null) {
            OfflineBudgetItemsRepository(
                BudgetDatabase.getDatabase(context).budgetItemDao())
        } else {
            OfflineBudgetItemsRepository(
                BudgetDatabase.getDatabase(context, file).budgetItemDao()
            )
        }
    }
}