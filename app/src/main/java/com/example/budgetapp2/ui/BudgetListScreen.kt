package com.example.budgetapp2.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.budgetapp2.data.BudgetItem
import com.example.budgetapp2.data.valueToCurrency

@Composable
fun BudgetListScreen(
    viewModel: BudgetListViewModel = viewModel(factory = ViewModelProvider.Factory),
    navController: NavController
) {
    //var expanded by remember { mutableStateOf(false) }
    //var sortBy by remember { mutableStateOf("Name (A-Z)") }
    val budgetListUiState by viewModel.budgetListUiState.collectAsState()
    Column{
        // show expenses or incomes?
        SingleChoiceSegmentedButtonRow(modifier = Modifier
            .padding(1.dp)
            .fillMaxWidth()
        ) {
            SegmentedButton(
                selected = budgetListUiState.expensesOrIncomes == 0,
                onClick = { viewModel.updateButton(0) },
                shape = SegmentedButtonDefaults.itemShape(
                    index = 0,
                    count = 2
                ),
                label = { Text(text = "Expenses") },
            )
            SegmentedButton(
                selected = budgetListUiState.expensesOrIncomes == 1,
                onClick = { viewModel.updateButton(1) },
                shape = SegmentedButtonDefaults.itemShape(
                    index = 1,
                    count = 2
                ),
                label = { Text(text = "Incomes") },
            )
        }
        if (budgetListUiState.expensesOrIncomes == 0) {
            ListHeader(0, budgetListUiState, navController, viewModel)
        }
        else if (budgetListUiState.expensesOrIncomes == 1) {
            ListHeader(1, budgetListUiState, navController, viewModel)
        }
    }

}

@Composable
fun ListHeader(
    expensesOrIncomes: Int,
    budgetListUiState: BudgetListUiState,
    navController: NavController,
    viewModel: BudgetListViewModel
) {
    //var expanded by remember { mutableStateOf(false) }
    val sortExpanded = remember { mutableStateOf(false) }
    val filterExpanded = remember { mutableStateOf(false) }

    //var sortBy by remember { mutableStateOf("Name (A-Z)") }
    val sortBy = remember { mutableStateOf("Name (A-Z)") }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                FilterButton(
                    filterExpanded,
                    budgetListUiState,
                    viewModel
                )
                UpdateApisButton(
                    viewModel
                )
            }

            SortByButton(
                sortExpanded,
                sortBy
            )
        }



    }

    when (sortBy.value) {
        "Name (A-Z)" -> CategoriesList(
            budgetListUiState.budgetItemList
                .filter {if (expensesOrIncomes == 0) {
                    it.expenseOrIncome == 0
                } else {
                    it.expenseOrIncome == 1
                }}
                .sortedBy { it.name }
                .groupBy { it.category }
                .values
                .toList(),
            navController,
            viewModel,
            budgetListUiState,
            expensesOrIncomes)
        "Name (Z-A)" -> CategoriesList(
            budgetListUiState.budgetItemList
                .filter {if (expensesOrIncomes == 0) {
                    it.expenseOrIncome == 0
                } else {
                    it.expenseOrIncome == 1
                }}
                .sortedByDescending { it.name }
                .groupBy { it.category }
                .values
                .toList(),
            navController,
            viewModel,
            budgetListUiState,
            expensesOrIncomes)
        "Value (Low to High)" -> CategoriesList(
            budgetListUiState.budgetItemList
                .filter {if (expensesOrIncomes == 0) {
                    it.expenseOrIncome == 0
                } else {
                    it.expenseOrIncome == 1
                }}
                .sortedBy { it.value }
                .groupBy { it.category }
                .values
                .toList(),
            navController,
            viewModel,
            budgetListUiState,
            expensesOrIncomes)
        "Value (High to Low)" -> CategoriesList(
            budgetListUiState.budgetItemList
                .filter {if (expensesOrIncomes == 0) {
                    it.expenseOrIncome == 0
                } else {
                    it.expenseOrIncome == 1
                }}
                .sortedByDescending { it.value }
                .groupBy { it.category }
                .values
                .toList(),
            navController,
            viewModel,
            budgetListUiState,
            expensesOrIncomes)
    }
}

@Composable
fun SortByButton(
    expanded: MutableState<Boolean>,
    sortBy: MutableState<String>,
) {
    Column(
        modifier = Modifier.width(300.dp),
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { expanded.value = !expanded.value },
                modifier = Modifier.width(300.dp)
            ) {
                Text(
                    text = "Sort by: ${sortBy.value} ▼"
                )
            }
        }
        Box(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false },
                modifier = Modifier

            ) {
                DropdownMenuItem(
                    text = { Text("Name (A-Z)") },
                    onClick = {
                        sortBy.value = "Name (A-Z)"
                    }
                )
                DropdownMenuItem(
                    text = { Text("Name (Z-A)") },
                    onClick = {
                        sortBy.value = "Name (Z-A)"
                    }
                )
                DropdownMenuItem(
                    text = { Text("Value (Low to High)") },
                    onClick = {
                        sortBy.value = "Value (Low to High)"
                    }
                )
                DropdownMenuItem(
                    text = { Text("Value (High to Low)") },
                    onClick = {
                        sortBy.value = "Value (High to Low)"
                    }
                )
            }
        }
    }
}

@Composable
fun FilterButton(
    expanded: MutableState<Boolean>,
    budgetListUiState: BudgetListUiState,
    viewModel: BudgetListViewModel
) {
    Column(
        modifier = Modifier.width(200.dp),
    ) {
        Button(
            onClick = { expanded.value = !expanded.value },
            modifier = Modifier.width(200.dp)
        ) {
            Text(
                text = "Filter ▼"
            )
        }
        Box(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false },
                modifier = Modifier
            ) {
                if (budgetListUiState.expensesOrIncomes == 0) {
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Quantity")
                                Checkbox(
                                    checked = budgetListUiState.showQuantity,
                                    onCheckedChange = {
                                        viewModel.updateShowQuantity(it)
                                    }
                                )
                            }
                        },
                        onClick = {
                            viewModel.updateShowQuantity(!budgetListUiState.showQuantity)
                        }
                    )
                }

                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Frequency")
                            Checkbox(
                                checked = budgetListUiState.showFrequency,
                                onCheckedChange = {
                                    viewModel.updateShowFrequency(it)
                                }
                            )
                        }
                    },
                    onClick = {
                        viewModel.updateShowFrequency(!budgetListUiState.showFrequency)
                    }
                )
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Cost per Week")
                            Checkbox(
                                checked = budgetListUiState.showCostPerWeek,
                                onCheckedChange = {
                                    viewModel.updateShowCostPerWeek(it)
                                }
                            )
                        }
                    },
                    onClick = {
                        viewModel.updateShowCostPerWeek(!budgetListUiState.showCostPerWeek)
                    }
                )
            }
        }
    }
}

@Composable
fun UpdateApisButton(
    viewModel: BudgetListViewModel
) {
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            viewModel.updateApis()
        }
    ) {
        Text(text = "Update APIs")
    }
}

@Composable
fun CategoriesList(listByCategory: List<List<BudgetItem>>,
                   navController: NavController,
                   viewModel: BudgetListViewModel,
                   budgetListUiState: BudgetListUiState,
                   expensesOrIncomes: Int = 0
) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        Log.i("num sections", "${listByCategory.size}")
        items(listByCategory.size) { index ->
            CategorySection(
                listByCategory[index],
                listByCategory[index][0].category,//.toString(),
                navController,
                viewModel,
                budgetListUiState
            )
        }
    }
}

@Composable
fun CategorySection(thisCategoryItemsList: List<BudgetItem>,
                    headerName: String = "Expenses",
                    navController: NavController,
                    viewModel: BudgetListViewModel,
                    budgetListUiState: BudgetListUiState,
                    expensesOrIncomes: Int = 0
) {
    var isOpen by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = Modifier
    ) {
        Card(
            modifier = Modifier
                .padding(2.dp)
                .fillMaxWidth()
        ) {
            CategoryHeader(headerName, { isOpen = !isOpen }, isOpen)
        }
        if (isOpen) {
            SubcategoriesList(
                thisCategoryItemsList
                    .sortedBy { it.name }
                    .groupBy { it.subcategory }
                    .values
                    .toList(),
                navController,
                viewModel,
                budgetListUiState
            )

        }
    }
}

@Composable
fun SubcategoriesList(
    subcategoriesList: List<List<BudgetItem>>,
    navController: NavController,
    viewModel: BudgetListViewModel,
    budgetListUiState: BudgetListUiState
) {
    for (i in subcategoriesList.indices) {
        SubcategorySection(
            subcategoriesList[i],
            subcategoriesList[i][0].subcategory,//.toString(),
            navController,
            viewModel,
            budgetListUiState
        )
    }
}

@Composable
fun SubcategorySection(
    itemsList: List<BudgetItem>,
    headerName: String = "Expenses",
    navController: NavController,
    viewModel: BudgetListViewModel,
    budgetListUiState: BudgetListUiState
) {
    var isOpen by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = Modifier
    ) {
        Card(
            modifier = Modifier
                .padding(2.dp)
                .fillMaxWidth(),
            shape = RectangleShape
        ) {
            SubcategoryHeader(
                headerName,
                { isOpen = !isOpen },
                isOpen
            )
        }
        if (isOpen) {
            BudgetList(
                itemsList,
                navController,
                viewModel,
                budgetListUiState)
            HorizontalDivider(thickness = 1.dp)
        }
    }
}

@Composable
fun BudgetList(
    budgetItemList: List<BudgetItem>,
    navController: NavController,
    viewModel: BudgetListViewModel,
    budgetListUiState: BudgetListUiState,
    modifier: Modifier = Modifier,
) {
//    LazyColumn(modifier = modifier) {
//        items(budgetItemList.size) { index ->
//            ListItem(budgetItemList[index],
//                navController,
//                viewModel,
//                budgetListUiState
//            )
//        }
//    }
    Column(modifier = modifier) {
        for (i in budgetItemList.indices) {
            //items(budgetItemList.size) { index ->
            ListItem(budgetItemList[i],
                navController,
                viewModel,
                budgetListUiState
            )
        }
    }
}





@Composable
fun CategoryHeader(text: String,
                   clickEffect: () -> Unit,
                   open: Boolean,
                   padding: Int = 16,
                   style: TextStyle = MaterialTheme.typography.headlineMedium,
) {
    Row(modifier = Modifier
        .clickable { clickEffect() }
        .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text,
            style = style,
            modifier = Modifier
                .padding(padding.dp)
        )
        if (open) {
            Text(
                text = "▲",
                style = style,
                modifier = Modifier
                    .padding(padding.dp)
            )
        }
        else {
            Text(
                text = "▼",
                style = style,
                modifier = Modifier
                    .padding(padding.dp)
            )
        }
    }
}

@Composable
fun SubcategoryHeader(text: String,
                   clickEffect: () -> Unit,
                   open: Boolean,
) {
    Row(modifier = Modifier
        .clickable { clickEffect() }
        .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineSmall,
            fontStyle = FontStyle.Italic,
            modifier = Modifier
                .padding(top = 4.dp, bottom = 4.dp,
                    start = 16.dp,end = 16.dp)
        )
        if (open) {
            Text(
                text = "▲",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(4.dp)
            )
        }
        else {
            Text(
                text = "▼",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(4.dp)
            )
        }
    }
}





@Composable
fun ListItem(
    budgetItem: BudgetItem,
    navController: NavController,
    viewModel: BudgetListViewModel,
    budgetListUiState: BudgetListUiState
) {
    OutlinedCard(
        shape = RectangleShape,
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth()
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = budgetItem.name,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                    //.fillMaxWidth()
                )

                Button(
                    onClick = {
                        navController.navigate(
                            "EditItem/${budgetItem.id}"
                        ) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                ) {
                    Icon(
                        Icons.Rounded.Edit,
                        contentDescription = "Edit Item",
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = if (budgetListUiState.expensesOrIncomes == 0) {
                        "Cost: " + valueToCurrency(budgetItem.value) + " per item"
                    } else {
                        "Income: " + valueToCurrency(budgetItem.value)
                    },
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                    //.fillMaxWidth()
                )
                Button(
                    onClick = {
                        viewModel.removeItem(budgetItem)
                    }
                ) {
                    Icon(
                        Icons.Rounded.Delete,
                        contentDescription = "Delete Item",
                    )
                }
            }
            if (budgetListUiState.showQuantity && budgetListUiState.expensesOrIncomes == 0) {
                Text(
                    text = "Quantity: " + budgetItem.quantity.toString() + " items per frequency period",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            if (budgetListUiState.showFrequency) {
                Text(
                    text = "Frequency: Every " + budgetItem.frequency.toString() + " days",
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            if (budgetListUiState.showCostPerWeek) {
                if (budgetListUiState.expensesOrIncomes == 0) {
                    Text(
                        text = "Cost per week: " + valueToCurrency(budgetItem.valuePerDay * 7.0),
                        style = MaterialTheme.typography.titleMedium,
                    )
                } else {
                    Text(
                        text = "Income per week: " + valueToCurrency(budgetItem.valuePerDay * 7.0),
                        style = MaterialTheme.typography.titleMedium,
                    )
                }

            }
        }
    }
}







