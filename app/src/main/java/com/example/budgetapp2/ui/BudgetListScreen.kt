package com.example.budgetapp2.ui

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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.budgetapp2.data.BudgetItem
import com.example.budgetapp2.data.amountToCurrency

@Composable
fun BudgetListScreen(
    viewModel: BudgetListViewModel = viewModel(factory = ViewModelProvider.Factory),
    navController: NavController
) {
    //var expanded by remember { mutableStateOf(false) }
    //var sortBy by remember { mutableStateOf("Name (A-Z)") }
    val budgetListUiState by viewModel.budgetListUiState.collectAsState()
    Column{
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
            ExpensesHeader(0, budgetListUiState, navController, viewModel)
        }
        else if (budgetListUiState.expensesOrIncomes == 1) {
            ExpensesHeader(1, budgetListUiState, navController, viewModel)
        }
    }

}

@Composable
fun ExpensesHeader(
    expensesOrIncomes: Int,
    budgetListUiState: BudgetListUiState,
    navController: NavController,
    viewModel: BudgetListViewModel
) {
    var expanded by remember { mutableStateOf(false) }
    var sortBy by remember { mutableStateOf("Name (A-Z)") }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column{
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Sort by: $sortBy ▼"
                    )
                }
            }
            Box(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier

                ) {
                    DropdownMenuItem(
                        text = { Text("Name (A-Z)") },
                        onClick = {
                            sortBy = "Name (A-Z)"
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Name (Z-A)") },
                        onClick = {
                            sortBy = "Name (Z-A)"
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Cost (Low to High)") },
                        onClick = {
                            sortBy = "Cost (Low to High)"
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Cost (High to Low)") },
                        onClick = {
                            sortBy = "Cost (High to Low)"
                        }
                    )
                }
            }
        }
    }

    when (sortBy) {
        "Name (A-Z)" -> CategoryList(
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
            viewModel)
        "Name (Z-A)" -> CategoryList(
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
            viewModel)
        "Cost (Low to High)" -> CategoryList(
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
            viewModel)
        "Cost (High to Low)" -> CategoryList(
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
            viewModel)
    }
}

@Composable
fun IncomesHeader(
    budgetListUiState: BudgetListUiState,
    navController: NavController,
    viewModel: BudgetListViewModel
) {
    var expanded by remember { mutableStateOf(false) }
    var sortBy by remember { mutableStateOf("Name (A-Z)") }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column{
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Sort by: $sortBy ▼"
                    )
                }
            }
            Box(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier

                ) {
                    DropdownMenuItem(
                        text = { Text("Name (A-Z)") },
                        onClick = {
                            sortBy = "Name (A-Z)"
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Name (Z-A)") },
                        onClick = {
                            sortBy = "Name (Z-A)"
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Cost (Low to High)") },
                        onClick = {
                            sortBy = "Cost (Low to High)"
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Cost (High to Low)") },
                        onClick = {
                            sortBy = "Cost (High to Low)"
                        }
                    )
                }
            }
        }
    }

    when (sortBy) {
        "Name (A-Z)" -> CategoryList(
            budgetListUiState.budgetItemList
                .sortedBy { it.name }
                .groupBy { it.category }
                .values
                .toList(),
            navController,
            viewModel)
        "Name (Z-A)" -> CategoryList(
            budgetListUiState.budgetItemList
                .sortedByDescending { it.name }
                .groupBy { it.category }
                .values
                .toList(),
            navController,
            viewModel)
        "Cost (Low to High)" -> CategoryList(
            budgetListUiState.budgetItemList
                .sortedBy { it.value }
                .groupBy { it.category }
                .values
                .toList(),
            navController,
            viewModel)
        "Cost (High to Low)" -> CategoryList(
            budgetListUiState.budgetItemList
                .sortedByDescending { it.value }
                .groupBy { it.category }
                .values
                .toList(),
            navController,
            viewModel)
    }
}

@Composable
fun CategoryList(sectionsList: List<List<BudgetItem>>, navController: NavController, viewModel: BudgetListViewModel) {
    Column(modifier = Modifier.fillMaxWidth()) {
        for (i in sectionsList.indices) {
            sectionsList[i][0].category?.let {
                ExpandableSection(sectionsList[i],
                    it, navController, viewModel)
            }
        }
    }
    //ExpandableSection(sectionsList[0], "Expenses")
}

@Composable
fun ExpandableHeader(text: String, clickEffect: () -> Unit, open: Boolean) {
    Row(modifier = Modifier
        .clickable { clickEffect() }
        .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .padding(16.dp)
        )
        if (open) {
            Text(
                text = "▲",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .padding(16.dp)
            )
        }
        else {
            Text(
                text = "▼",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun ExpandableSection(itemsList: List<BudgetItem>, headerName: String = "Expenses", navController: NavController, viewModel: BudgetListViewModel) {
    var isOpen by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = Modifier
    ) {
        Card(
            modifier = Modifier
                .padding(2.dp)
                .fillMaxWidth()
        ) {
            ExpandableHeader(headerName, { isOpen = !isOpen }, isOpen)
        }
        if (isOpen) {
            BudgetList(itemsList, navController, viewModel)
            HorizontalDivider(thickness = 1.dp)
        }
    }
}

@Composable
fun ListItem(budgetItem: BudgetItem, navController: NavController, viewModel: BudgetListViewModel) {
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
                Text(
                    text = amountToCurrency(budgetItem.value),
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
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Quantity: " + budgetItem.quantity.toString(),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                //.fillMaxWidth()
                )
                Text(
                    text = "Frequency: " + budgetItem.frequency.toString(),
                    style = MaterialTheme.typography.headlineSmall,
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
        }
    }
}

@Composable
fun BudgetList(
    budgetItemList: List<BudgetItem>,
    navController: NavController,
    viewModel: BudgetListViewModel,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        items(budgetItemList.size) { index ->
            ListItem(budgetItemList[index], navController, viewModel)
        }
    }
}



