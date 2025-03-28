package com.example.budgetapp2.data

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.budgetapp2.BudgetApplication
import com.example.budgetapp2.network.BudgetApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.Retrofit
import java.io.File
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType

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

    fun getDatabase(context: Context = application): BudgetDatabase {
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
        database = BudgetDatabase.getDatabase(application, file)
        repository = OfflineBudgetItemsRepository(database!!.budgetItemDao(), budgetApiInterface)

    }

    fun updateRepository() {
        repository = OfflineBudgetItemsRepository(database!!.budgetItemDao(), budgetApiInterface)
    }

    fun resetDatabase(context: Context) {
        database!!.close()
        database = createDatabase(context)

    }
}