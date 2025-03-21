package com.example.budgetapp2.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.budgetapp2.data.ExportActivity
import com.example.budgetapp2.data.ImportActivity

@Composable
fun ExportOrImportButton(viewModel: SettingsViewModel) {
    SingleChoiceSegmentedButtonRow {
        SegmentedButton(
            selected = viewModel.optionsUiState.buttonIndex == 0,
            onClick = { viewModel.updateButton(0) },
            shape = SegmentedButtonDefaults.itemShape(
                index = 0,
                count = 2
            ),
            label = { Text(text = "Export") },
        )
        SegmentedButton(
            selected = viewModel.optionsUiState.buttonIndex == 1,
            onClick = { viewModel.updateButton(1) },
            shape = SegmentedButtonDefaults.itemShape(
                index = 1,
                count = 2
            ),
            label = { Text(text = "Import") },
        )
    }

}

@Composable
fun SettingsScreen() {
    val viewModel: SettingsViewModel = viewModel(factory = ViewModelProvider.Factory)
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxWidth()
        , horizontalAlignment = Alignment.CenterHorizontally) {
        ExportOrImportButton(viewModel)
        if (viewModel.optionsUiState.buttonIndex == 0) {
            ExportButton(context)
        }
        else {
           ImportButton(viewModel, context)
        }
    }
}

@Composable
fun ExportButton(context: Context) {
    Button(onClick = {
        val intent = Intent(context, ExportActivity::class.java)
        context.startActivity(intent)
    }
    ) {
        Text(text = "Export")
    }
}

@Composable
fun ImportButton(viewModel: SettingsViewModel, context: Context) {
    var iuri: Uri? = null
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument(),
        onResult = {
            uri ->
            iuri = uri
        }
    )
    Button(onClick = {
        val intent = Intent(context, ImportActivity::class.java)
        //intent.putExtra("viewModel", viewModel)
        //context.startActivity(intent)
        launcher.launch(arrayOf("*/*"))
        if (iuri != null) {
            Log.i("uri", iuri.toString())
        }
    }
    ) {
        Text(text = "Import")
    }
}