package com.example.budgetapp2.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsEndWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.max

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

    val isDropDownExpanded = remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            SingleChoiceSegmentedButtonRow(modifier = Modifier
                .padding(1.dp).fillMaxWidth()
            ) {
                SegmentedButton(
                    selected = viewModel.expenseUiState.buttonIndex == 0,
                    onClick = { viewModel.updateButton(0) },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = 0,
                        count = 2
                    ),
                    label = { Text(text = "Expense") },
                )
                SegmentedButton(
                    selected = viewModel.expenseUiState.buttonIndex == 1,
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
                        viewModel.enterExpense()
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
                        value = viewModel.expenseUiState.name,
                        onValueChange = viewModel::updateName,
                        label = { Text("Item Name") },
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = viewModel.expenseUiState.cost.toString(),
                        onValueChange = viewModel::updateCost,
                        label = { Text("Item Cost") },
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = viewModel.expenseUiState.amount,
                        onValueChange = viewModel::updateAmount,
                        label = { Text("Item Amount (per frequency period)") },
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
                        value = viewModel.expenseUiState.frequency.toString(),
                        onValueChange = { viewModel.updateFrequency(it.toIntOrNull() ?: 0) },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Expense Frequency") },
                        trailingIcon = {
                            if (isDropDownExpanded.value) {
                                Icon(Icons.Filled.KeyboardArrowUp,
                                    contentDescription = "description",
                                    Modifier.clickable {
                                        isDropDownExpanded.value = !isDropDownExpanded.value
                                    }
                                )
                            } else {
                                Icon(Icons.Filled.KeyboardArrowDown,
                                    contentDescription = "description",
                                    Modifier.clickable {
                                        isDropDownExpanded.value = !isDropDownExpanded.value
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
                            expanded = isDropDownExpanded.value,
                            onDismissRequest = { isDropDownExpanded.value = false },
                            //modifier = Modifier.fillMaxWidth()
                        ) {
                            DropdownMenuItem(
                                text = { Text("Daily") },
                                onClick = {
                                    isDropDownExpanded.value = false
                                    viewModel.updateFrequency(1)
                                },
                            )
                            DropdownMenuItem(
                                text = { Text("Weekly") },
                                onClick = {
                                    isDropDownExpanded.value = false
                                    viewModel.updateFrequency(7)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Monthly") },
                                onClick = {
                                    isDropDownExpanded.value = false
                                    viewModel.updateFrequency(30)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Yearly") },
                                onClick = {
                                    isDropDownExpanded.value = false
                                    viewModel.updateFrequency(365)
                                }
                            )
                        }
                    }
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        Text("Use API Key?")
                        Checkbox(
                            checked = viewModel.expenseUiState.useApiKey,
                            onCheckedChange = {
                                viewModel.switchUseApiKey()
                            },
                            modifier = Modifier.fillMaxWidth()

                        )
                    }
                    if (viewModel.expenseUiState.useApiKey) {
                        OutlinedTextField(
                            value = viewModel.expenseUiState.apiKey,
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