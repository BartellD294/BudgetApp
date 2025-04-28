package com.example.budgetapp2.ui

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.RoomDatabase
import com.example.budgetapp2.BudgetApplication
import kotlinx.coroutines.launch
import java.io.FileOutputStream
import java.io.Serializable


class SettingsViewModel(private val application: BudgetApplication): ViewModel(), Serializable {
    var optionsUiState by mutableStateOf(OptionsUiState(0, null, null))
    private var importUri by mutableStateOf<Uri?>(null)
    private var exportUri by mutableStateOf<Uri?>(null)
    fun updateButton(index: Int) {
        optionsUiState = optionsUiState.copy(buttonIndex = index)
    }

//    fun exportDatabase(uri: Uri) {
//        val intent: Intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
//            addCategory(Intent.CATEGORY_OPENABLE)
//            setType("text/plain")
//        }
//    }
    fun updateImportUri(uri: Uri) {
        Log.i("destination button uri", uri.toString())
        importUri = uri
    }
    fun updateExportUri(uri: Uri) {
        exportUri = uri
    }
    fun resetDestinations() {
        importUri = null
        exportUri = null
    }

    fun updateApis() {
        viewModelScope.launch {
            application.container.repository.updateItemsWithApiIds()
        }
    }

    // ContentResolver (ContentResolver.openInputStream(Uri)):
    // https://developer.android.com/reference/android/content/ContentResolver#openInputStream(android.net.Uri)
    // Using input stream:
    // https://developer.android.com/reference/java/io/InputStream
    fun importDatabase(context: Context) {
        //val tempFile = File.createTempFile("imported_database", ".db")

        Log.i("start import uri", importUri.toString())
        Log.i("start import uri path", importUri!!.path.toString())
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(importUri!!)
        //val outputStream = contentResolver.openOutputStream(exportUri!!)
        val dbFile = context.getDatabasePath("budget_item_database")
        application.container.closeDatabase()
        //application.container.deleteDatabase()
        inputStream?.use { input ->
            FileOutputStream(dbFile).use { output ->
                input.copyTo(output)
            }
        }
        //Log.i("imported temp file", tempFile.toString())



        application.container.updateDatabase(file = dbFile)
        //application.container.resetDatabase(context)
        application.container.updateRepository()
//        viewModelScope.launch {
//            application.container.reload()
//        }
        application.restartApp()

    }

    fun exportDatabase(context: Context) {
        application.container.closeDatabase()
        val outputStream = context.contentResolver.openOutputStream(exportUri!!)
        val inputStream = context.getDatabasePath("budget_item_database").inputStream()
        inputStream.use { input ->
            outputStream.use { output ->
                input.copyTo(output!!)
            }
        }
        application.container.updateDatabase()
        application.container.updateRepository()
        application.restartApp()
    }

}

//private fun writeFileToUri(uri: Uri, context: Context) {
//    val inStream = context.getDatabasePath("budget_item_database").inputStream()
//    val outStream = context.contentResolver.openOutputStream(uri)
//    inStream.use { input ->
//        outStream.use { output ->
//            input.copyTo(output!!)
//        }
//    }
//}

data class OptionsUiState(val buttonIndex: Int,
val importDatabase: RoomDatabase?,
    val exportDatabase: RoomDatabase?)