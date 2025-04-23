package com.example.budgetapp2.ui

import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun ExportOrImportButton(viewModel: SettingsViewModel) {
    SingleChoiceSegmentedButtonRow {
        SegmentedButton(
            selected = viewModel.optionsUiState.buttonIndex == 0,
            onClick = { viewModel.updateButton(0)
                      viewModel.resetDestinations()},
            shape = SegmentedButtonDefaults.itemShape(
                index = 0,
                count = 2
            ),
            label = { Text(text = "Export") },
        )
        SegmentedButton(
            selected = viewModel.optionsUiState.buttonIndex == 1,
            onClick = { viewModel.updateButton(1)
                      viewModel.resetDestinations()},
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
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = "Note: App will close and must be reopened after export or import."
        )
        if (viewModel.optionsUiState.buttonIndex == 0) {
            ExportDestinationButton(viewModel, context)
            //StartExportButton(context)
        }
        else {
            ImportDestinationButton(viewModel, context)
            StartImportButton(viewModel, context)
        }
        UpdateApisButton(viewModel)
    }
}

@Composable
fun UpdateApisButton(viewModel: SettingsViewModel) {
    Button(onClick = {
        viewModel.updateApis()
    }) {
        Text(text = "Update APIs")
    }
}

@Composable
fun ExportDestinationButton(viewModel: SettingsViewModel, context: Context) {
    val exportLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.CreateDocument("*/*"),
            onResult = {
                it.let { resultUri ->
                    Log.i("destination button uri", resultUri.toString())
                    viewModel.updateExportUri(resultUri!!)
                    viewModel.exportDatabase(context)
                }
            }
        )
    Button(onClick = {
        exportLauncher.launch("exportname")
    })
    {
        Text(text = "Select Export Destination")
    }
}




@Composable
fun ImportDestinationButton(viewModel: SettingsViewModel, context: Context) {
    //var destination_uri: Uri? = null
    val importLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.OpenDocument(),
            onResult = {
                it.let { resultUri ->
                    viewModel.updateImportUri(resultUri!!)
                }
            }
        )
    Button(onClick = {
        importLauncher.launch(arrayOf("*/*"))
    }) {
        Text(text = "Select Import Destination")
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartImportButton(viewModel: SettingsViewModel, context: Context) {
    val showAlert = remember { mutableStateOf(false) }
    Button(onClick = {
        showAlert.value = true
        //viewModel.importDatabase(context)
    } ) {
        Text(text = "Import")
    }
    if (showAlert.value) {
        AlertDialog(
            onDismissRequest = { showAlert.value = false },
            title = { Text(text = "Import") },
            text = {
                Text(
                    text = "Are you sure you want to import?\n" +
                    "Importing a new database will overwrite the current database.\n" +
                    "It is recommended to backup your current database before importing." +
                    "Upon import, the app will close and require a restart."
                )
                   },
            confirmButton = {
                TextButton(
                    onClick = {
                        showAlert.value = false
                        viewModel.importDatabase(context)
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = {showAlert.value = false }) { Text("Dismiss")}
            }
        )

    }
}

