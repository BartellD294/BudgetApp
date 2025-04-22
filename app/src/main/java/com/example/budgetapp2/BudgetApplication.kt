package com.example.budgetapp2

import android.app.Activity
import android.app.Application
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.budgetapp2.data.AppDataContainer

class BudgetApplication : Application() {
    lateinit var container: AppDataContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }

    fun restartApp() {
        Runtime.getRuntime().exit(0)

    }
}