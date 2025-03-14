package com.example.budgetapp2.ui

import android.content.Context
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
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ExportOrImportButton(viewModel: SettingsViewModel, context: Context) {
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

    Column(
        modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ExportOrImportButton(viewModel, context)
        if (viewModel.optionsUiState.buttonIndex == 0) {
            ExportButton(viewModel, context)
        } else {

            //ImportButton(viewModel, context)

        }
    }
}

@Composable
fun ExportButton(viewModel: SettingsViewModel, context: Context) {
    Button(modifier = Modifier,
        onClick = {
            val activity: ExportActivity = ExportActivity()
            activity.startExport()
        }
    ) {
        Text(text = "Export Database")
    }
}
