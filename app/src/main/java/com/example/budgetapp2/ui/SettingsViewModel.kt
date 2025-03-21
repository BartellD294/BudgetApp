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
import androidx.room.RoomDatabase
import com.example.budgetapp2.data.BudgetItemsRepository
import java.io.Serializable

class SettingsViewModel(repository: BudgetItemsRepository): ViewModel(), Serializable {
    var optionsUiState by mutableStateOf(OptionsUiState(0, null, null))
    var import_uri by mutableStateOf<Uri?>(null)
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
        import_uri = uri
        Log.i("uri", uri.toString())
    }
}


fun openFile(pickerInitialUri: Uri) {
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = "application/pdf"

    }

}

/*
class ExportActivity(): Activity() {
    val CREATE_FILE = 1
    fun exportDatabase() {
        val intent: Intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            setType("text/plain")
        }
        startActivityForResult(intent, CREATE_FILE)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        resultData: Intent?
    ) {
        if (requestCode == CREATE_FILE && resultCode == RESULT_OK) {
            resultData?.data?.also { uri ->
                writeFileToUri(uri, this)
            }
        }
        super.onActivityResult(requestCode, resultCode, resultData)
    }
}



private fun writeFileToUri(uri:Uri, context: Context) {
    val inStream = context.getDatabasePath("budget_item_database").inputStream()
    val outStream = context.contentResolver.openOutputStream(uri)
    inStream.use { input ->
        outStream.use { output ->
            input.copyTo(output!!)
        }
    }
}
 */

data class OptionsUiState(val buttonIndex: Int,
val importDatabase: RoomDatabase?,
    val exportDatabase: RoomDatabase?)