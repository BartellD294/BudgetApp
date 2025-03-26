package com.example.budgetapp2.data

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.budgetapp2.BudgetApplication
import com.example.budgetapp2.network.BudgetApiInterface
import retrofit2.Retrofit
import java.io.File
import retrofit2.converter.moshi.MoshiConverterFactory

/*
interface AppContainer {
    var repository: BudgetItemsRepository?
}

*/
class AppDataContainer(
    private val application: BudgetApplication,
    private val file: File? = null
) {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.stlouisfed.org/fred/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
    val budgetApiInterface: BudgetApiInterface = retrofit.create(BudgetApiInterface::class.java)






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