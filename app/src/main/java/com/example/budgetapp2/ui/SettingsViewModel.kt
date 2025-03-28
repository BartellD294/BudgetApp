package com.example.budgetapp2.ui

import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import android.app.Activity
import android.app.Application
import android.app.ComponentCaller
import android.net.Uri
import android.provider.DocumentsContract
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.RoomDatabase
import com.example.budgetapp2.BudgetApplication
import com.example.budgetapp2.data.AppDataContainer
import com.example.budgetapp2.data.BudgetItemsRepository
import kotlinx.coroutines.launch
import java.io.File
import java.io.Serializable


class SettingsViewModel(val application: BudgetApplication): ViewModel(), Serializable {
    var optionsUiState by mutableStateOf(OptionsUiState(0, null, null))
    var import_uri by mutableStateOf<Uri?>(null)
    var export_uri by mutableStateOf<Uri?>(null)
    fun updateButton(index: Int) {
        optionsUiState = optionsUiState.copy(buttonIndex = index)
    }

    fun exportDatabase(uri: Uri) {
        val intent: Intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            setType("text/plain")
        }
    }
    fun updateImportUri(uri: Uri) {
        Log.i("destination button uri", uri.toString())
        import_uri = uri
    }
    fun updateExportUri(uri: Uri) {
        export_uri = uri
    }
    fun resetDestinations() {
        import_uri = null
        export_uri = null
    }

    fun updateApis() {
        viewModelScope.launch {
            application.container.repository.updateItemsWithApiIds()
        }
    }

    fun importDatabase(context: Context) {
        Log.i("start import uri", import_uri!!.path.toString())
        val file = File(import_uri!!.path!!)
        val dbPath = context.getDatabasePath("budget_item_database")
        Log.i("db path", dbPath.toString())
        application.container.getDatabase(context).close()
        file.copyTo(dbPath, overwrite = true)

        application.container.resetDatabase(context)

        application.container.updateRepository()
    }
    fun exportDatabase(context: Context) {
        writeFileToUri(export_uri!!, context)
    }

}


fun openFile(pickerInitialUri: Uri) {
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = "application/pdf"

    }

}

private fun writeFileToUri(uri: Uri, context: Context) {
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