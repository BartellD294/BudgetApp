package com.example.budgetapp2.data

import android.util.Log
import com.example.budgetapp2.BudgetApplication
import com.example.budgetapp2.network.BudgetApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import java.io.File

class AppDataContainer(
    private val application: BudgetApplication,
    // private val file: File? = null
) {
    private val json = Json { ignoreUnknownKeys = true }
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.stlouisfed.org/fred/")
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
    private val budgetApiInterface: BudgetApiService = retrofit.create(BudgetApiService::class.java)

    private var database: BudgetDatabase? = BudgetDatabase.getDatabase(application)
    /* override */ var repository: BudgetItemsRepository = OfflineBudgetItemsRepository(database!!.budgetItemDao(), budgetApiInterface)

    fun updateDatabase(file: File? = null) {
        Log.i("update database", "update database")
        database = BudgetDatabase.getDatabase(application, file)
    }
    //fun deleteDatabase() {
    //    application.deleteDatabase("budget_item_database")
    //}


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