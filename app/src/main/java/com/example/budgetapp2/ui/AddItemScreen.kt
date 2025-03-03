package com.example.budgetapp2.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import kotlin.math.max

@Composable
fun AddItemScreen(
    viewModel: AddItemViewModel = viewModel(factory = ViewModelProvider.Factory),
    popBackStack: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        OutlinedTextField(
            value = viewModel.expenseUiState.name,
            onValueChange = viewModel::updateName,
            label = { Text("Item Name") },
            maxLines = 1,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = viewModel.expenseUiState.category,
            onValueChange = viewModel::updateCategory,
            label = { Text("Item Category") },
            maxLines = 1,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = viewModel.expenseUiState.cost,
            onValueChange = viewModel::updateCost,
            label = { Text("Item Cost") },
            maxLines = 1,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                coroutineScope.launch {
                    viewModel.enterExpense()
                    popBackStack()
                }
            },
            modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enter Item")
            }
        /*
        ItemEntryBox(
            expenseUiState = viewModel.expenseUiState,
            onValueChange = viewModel::updateUiState,
            label = Text("Item Name").toString(),
            modifier = Modifier.fillMaxWidth()
        )
        ItemEntryBox(
            expenseUiState = viewModel.expenseUiState,
            onValueChange = viewModel::updateUiState,
            label = Text("Item Cost").toString(),
            modifier = Modifier.fillMaxWidth()
        )
        ItemEntryBox(
            expenseUiState = viewModel.expenseUiState,
            onValueChange = viewModel::updateUiState,
            label = Text("Item Category").toString(),
            modifier = Modifier.fillMaxWidth()
        )
        */
    }
}

@Composable
fun ItemEntryBox(
    label: String,
    expenseUiState: ExpenseUiState,
    onValueChange: (ExpenseUiState) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = expenseUiState.name,
        onValueChange = {onValueChange},
        label = { Text(label) },
        maxLines = 1,
        modifier = Modifier.padding(20.dp)

    )
}

/*
@Preview(showBackground = true)
@Composable
fun ItemEntryScreenPreview() {
    AddItemScreen()
}
 */