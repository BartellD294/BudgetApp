package com.example.budgetapp2.data

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.budgetapp2.ui.SettingsViewModel

class ImportActivity() : ComponentActivity() {
    //val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) {
    //    uri ->
    //    intent.putExtra("import_string", uri.toString())
    //}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //viewModel = intent.getSerializableExtra("viewModel", SettingsViewModel::class.java)!!
        //launcher.launch(arrayOf("*/*"))

    }
    fun end()
    {
        finish()
    }
}

