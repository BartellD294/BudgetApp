package com.example.budgetapp2

import android.app.Application
import com.example.budgetapp2.data.AppContainer

class BudgetApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}