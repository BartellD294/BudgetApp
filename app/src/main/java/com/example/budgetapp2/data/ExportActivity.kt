package com.example.budgetapp2.data

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts

class ExportActivity : ComponentActivity() {
    private val exportLauncher = registerForActivityResult(
        ActivityResultContracts.CreateDocument("*/*")) { uri ->
            writeFileToUri(uri!!, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exportLauncher.launch("budget_item_e")
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

