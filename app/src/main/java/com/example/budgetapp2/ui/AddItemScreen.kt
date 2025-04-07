package com.example.budgetapp2.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun AddItemScreen(
    viewModel: AddItemViewModel = viewModel(factory = ViewModelProvider.Factory),
    id: String? = null,
    popBackStack: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    if (id != null) {
        LaunchedEffect(true) {
            coroutineScope {
                launch {
                    viewModel.updateUiStateById(id.toInt())
                }
            }
        }
    }
    val expenseUiState by viewModel.expenseUiState.collectAsState()

    val isFrequencyDropDownExpanded = remember { mutableStateOf(false) }
    val isCategoryDropDownExpanded = remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            SingleChoiceSegmentedButtonRow(modifier = Modifier
                .padding(1.dp).fillMaxWidth()
            ) {
                SegmentedButton(
                    selected = expenseUiState.buttonIndex == 0,
                    onClick = { viewModel.updateButton(0) },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = 0,
                        count = 2
                    ),
                    label = { Text(text = "Expense") },
                )
                SegmentedButton(

                    selected = expenseUiState.buttonIndex == 1,
                    onClick = { viewModel.updateButton(1) },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = 1,
                        count = 2
                    ),
                    label = { Text(text = "Income") },
                )
            }
        },
        bottomBar = {
            Button(
                onClick = {
                    coroutineScope.launch {
                        viewModel.enterItem()
                        popBackStack()
                    }
                },
                modifier = Modifier.padding(16.dp)
                    .fillMaxWidth(),
            ) {
                Text("Enter Item")
            }
        },
        content = { innerPadding ->
            Surface(modifier = Modifier.fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()))
            {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = expenseUiState.name,
                        onValueChange = viewModel::updateName,
                        label = { Text("Item Name") },
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = expenseUiState.value.toString(),
                        onValueChange = viewModel::updateValue,
                        label = { Text("Item Cost") },
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = expenseUiState.quantity,
                        onValueChange = viewModel::updateQuantity,
                        label = { Text("Item Amount (per frequency period)") },
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = expenseUiState.category,
                        onValueChange = viewModel::updateCategory,
                        label = { Text("Item Category") },
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            if (isCategoryDropDownExpanded.value) {
                                Icon(Icons.Filled.KeyboardArrowUp,
                                    contentDescription = "Category Open",
                                    Modifier.clickable {
                                        isCategoryDropDownExpanded.value = !isCategoryDropDownExpanded.value
                                    }
                                )
                            } else {
                                Icon(Icons.Filled.KeyboardArrowDown,
                                    contentDescription = "Category Closed",
                                    Modifier.clickable {
                                        isCategoryDropDownExpanded.value = !isCategoryDropDownExpanded.value
                                    }
                                )
                            }
                        }
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.TopEnd)
                    ) {
                        DropdownMenu(
                            expanded = isCategoryDropDownExpanded.value,
                            onDismissRequest = { isCategoryDropDownExpanded.value = false }
                        ) {
                            for (i in expenseUiState.categoryList.indices) {
                                DropdownMenuItem(
                                    text = { Text(expenseUiState.categoryList[i]) },
                                    onClick = {
                                        isCategoryDropDownExpanded.value = false
                                        viewModel.updateCategory(expenseUiState.categoryList[i])
                                    }
                                )
                            }
                        }
                    }


                    OutlinedTextField(
                        value = expenseUiState.frequency.toString(),
                        onValueChange = { viewModel.updateFrequency(it.toIntOrNull() ?: 0) },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Expense Frequency") },
                        trailingIcon = {
                            if (isFrequencyDropDownExpanded.value) {
                                Icon(Icons.Filled.KeyboardArrowUp,
                                    contentDescription = "description",
                                    Modifier.clickable {
                                        isFrequencyDropDownExpanded.value = !isFrequencyDropDownExpanded.value
                                    }
                                )
                            } else {
                                Icon(Icons.Filled.KeyboardArrowDown,
                                    contentDescription = "description",
                                    Modifier.clickable {
                                        isFrequencyDropDownExpanded.value = !isFrequencyDropDownExpanded.value
                                    }
                                )
                            }
                        }
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.TopEnd)
                    ) {
                        DropdownMenu(
                            expanded = isFrequencyDropDownExpanded.value,
                            onDismissRequest = { isFrequencyDropDownExpanded.value = false },
                            //modifier = Modifier.fillMaxWidth()
                        ) {
                            for (i in frequencyList.indices) {
                                DropdownMenuItem(
                                    text = { Text(frequencyList[i].first) },
                                    onClick = {
                                        isFrequencyDropDownExpanded.value = false
                                        viewModel.updateFrequency(frequencyList[i].second)
                                    }
                                )
                            }
                        }
                    }
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        Text("Use API Key?")
                        Checkbox(
                            checked = expenseUiState.useApiKey,
                            onCheckedChange = {
                                viewModel.switchUseApiKey()
                            },
                            modifier = Modifier.fillMaxWidth()

                        )
                    }
                    if (expenseUiState.useApiKey) {
                        OutlinedTextField(
                            value = expenseUiState.apiKey,
                            onValueChange = viewModel::updateApiKey,
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("API Key") },
                            maxLines = 1,
                        )
                    }
                }
            }
        }
    )
}

val frequencyList = listOf(
    Pair("Daily", 1),
    Pair("Weekly", 7),
    Pair("Monthly", 31),
    Pair("Yearly", 365)
)

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