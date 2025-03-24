package com.example.budgetapp2

import android.app.Application
import com.example.budgetapp2.data.AppContainer
import com.example.budgetapp2.data.AppDataContainer
import java.io.File

class BudgetApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
    fun updateDatabase(file: File) {
        container = AppDataContainer(this, file)

    }
}