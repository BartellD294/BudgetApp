package com.example.budgetapp2.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity

class ExportActivity(): ComponentActivity() {
    val CREATE_FILE = 1
    fun startExport() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"
            putExtra(Intent.EXTRA_TITLE, "invoice.pdf")

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
                writeFileToUri(uri, applicationContext)
            }
        }
        super.onActivityResult(requestCode, resultCode, resultData)
    }


}