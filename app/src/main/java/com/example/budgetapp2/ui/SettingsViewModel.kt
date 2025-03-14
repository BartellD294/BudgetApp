package com.example.budgetapp2.ui

import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import android.app.Activity
import android.app.Application
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.room.RoomDatabase
import com.example.budgetapp2.data.BudgetItemsRepository


class SettingsViewModel(repository: BudgetItemsRepository): ViewModel() {
    var optionsUiState by mutableStateOf(OptionsUiState(0, null, null))
    fun updateButton(index: Int) {
        optionsUiState = optionsUiState.copy(buttonIndex = index)
    }

}


public fun writeFileToUri(uri:Uri, context: Context) {
    val inStream = context.getDatabasePath("budget_item_database").inputStream()
    val outStream = context.contentResolver.openOutputStream(uri)
    inStream.use { input ->
        outStream.use { output ->
            input.copyTo(output!!)
        }
    }
}

data class OptionsUiState(val buttonIndex: Int,
val importDatabase: RoomDatabase?,
    val exportDatabase: RoomDatabase?)