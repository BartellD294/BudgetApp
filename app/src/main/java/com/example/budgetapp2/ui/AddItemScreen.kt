package com.example.budgetapp2.ui

import android.util.Log
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
import com.example.budgetapp2.data.Subcategory
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
                    viewModel.updateEnterOrUpdate(1)
                }
            }
        }
    }
    val expenseUiState by viewModel.expenseUiState.collectAsState()

    val isFrequencyDropDownExpanded = remember { mutableStateOf(false) }
    val isCategoryDropDownExpanded = remember { mutableStateOf(false) }
    val isSubcategoryDropDownExpanded = remember { mutableStateOf(false) }
    val subcategoryList = remember { mutableStateOf(listOf<Subcategory>()) }


    Scaffold(
        topBar = {
            if (expenseUiState.enterOrUpdate == 0) {
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
                if (expenseUiState.enterOrUpdate == 0) {
                    Text("Enter Item")
                } else {
                    Text("Update Item")
                }
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
                    //Name field
                    OutlinedTextField(
                        value = expenseUiState.name,
                        onValueChange = viewModel::updateName,
                        label = { Text("Item Name") },
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth()
                    )
                    //Value field
                    OutlinedTextField(
                        value = expenseUiState.value.toString(),
                        onValueChange = viewModel::updateValue,
                        label = { Text("Item Value (per 1 item)") },
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth()
                    )
                    //Quantity field
                    OutlinedTextField(
                        value = expenseUiState.quantity,
                        onValueChange = viewModel::updateQuantity,
                        label = { Text("Item Quantity (per frequency period)") },
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = (expenseUiState.buttonIndex == 0)
                    )
                    //Category field
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
                    //Category dropdown menu
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
                                    text = { Text(expenseUiState.categoryList[i].name) },
                                    onClick = {
                                        Log.i("Category", "Clicked")
                                        isCategoryDropDownExpanded.value = false
                                        viewModel.updateCategory(expenseUiState.categoryList[i].name)
                                    }
                                )
                            }
                        }
                    }

                    //Subcategory field
                    OutlinedTextField(
                        enabled = expenseUiState.category.isNotEmpty(),
                        value = expenseUiState.subcategory,
                        onValueChange = viewModel::updateSubcategory,
                        label = { Text("Item Subcategory") },
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            if (isSubcategoryDropDownExpanded.value) {
                                Icon(Icons.Filled.KeyboardArrowUp,
                                    contentDescription = "Subcategory Open",
                                    Modifier.clickable {
                                        isSubcategoryDropDownExpanded.value = !isSubcategoryDropDownExpanded.value
                                    }
                                )
                            } else {
                                Icon(Icons.Filled.KeyboardArrowDown,
                                    contentDescription = "Subcategory Closed",
                                    Modifier.clickable {
                                        isSubcategoryDropDownExpanded.value = !isSubcategoryDropDownExpanded.value
                                    }
                                )
                            }
                        }
                    )
                    //Subcategory dropdown menu
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.TopEnd)
                    ) {
                        DropdownMenu(
                            expanded = isSubcategoryDropDownExpanded.value,
                            onDismissRequest = { isSubcategoryDropDownExpanded.value = false }
                        ) {
//                            for (i in expenseUiState.categoryList.indices) {
//                                if (expenseUiState.categoryList[i].name == expenseUiState.category) {
//                                    subcategoryList.value = expenseUiState.categoryList[i].subcategories
//                                }
//                            }
//                            for (i in subcategoryList.value.indices) {}
                            for (i in expenseUiState.subcategoryList.indices) {
                                DropdownMenuItem(
                                    text = { Text(expenseUiState.subcategoryList[i].name) },
                                    onClick = {
                                        isSubcategoryDropDownExpanded.value = false
                                        viewModel.updateSubcategory(expenseUiState.subcategoryList[i].name)
                                    }
                                )
                            }

                        }
                    }


                    //Frequency field
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
                    //Frequency dropdown menu
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
                    //API ID checkbox and field
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