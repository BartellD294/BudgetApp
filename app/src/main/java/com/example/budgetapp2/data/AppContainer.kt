package com.example.budgetapp2.data

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.budgetapp2.BudgetApplication
import com.example.budgetapp2.network.BudgetApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.File
import kotlin.coroutines.coroutineContext

class AppDataContainer(
    private val application: BudgetApplication,
    private val file: File? = null
) {
    private val json = Json { ignoreUnknownKeys = true }
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.stlouisfed.org/fred/")
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
    val budgetApiInterface: BudgetApiService = retrofit.create(BudgetApiService::class.java)

    var database: BudgetDatabase? = BudgetDatabase.getDatabase(application)
    /* override */ var repository: BudgetItemsRepository = OfflineBudgetItemsRepository(database!!.budgetItemDao(), budgetApiInterface)

    fun updateDatabase(file: File? = null) {
        Log.i("update database", "update database")
        database = BudgetDatabase.getDatabase(application, file)
    }
    fun deleteDatabase() {
        application.deleteDatabase("budget_item_database")
    }


    //val _reloadDatabase = MutableSharedFlow<Unit>()
    //val reloadDatabase: SharedFlow<Unit> = _reloadDatabase.asSharedFlow()

    //suspend fun reload() {
    //    Log.i("AppContainer", "reload function")
    //    CoroutineScope(coroutineContext).launch {
    //        _reloadDatabase.emit(Unit)
    //    }
    //}

    fun updateRepository() {
        repository = OfflineBudgetItemsRepository(database!!.budgetItemDao(), budgetApiInterface)
    }
    fun closeDatabase() {
        database!!.close()
    }
}