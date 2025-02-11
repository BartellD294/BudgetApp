package com.example.budgetapp2.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.budgetapp2.data.BudgetItem
import com.example.budgetapp2.data.amountToCurrency
import com.example.budgetapp2.data.tempBudgetItems
import com.example.budgetapp2.ui.theme.BudgetApp2Theme
import java.text.NumberFormat

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
                text = "▼",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .padding(16.dp)
            )
        }
        else {
            Text(
                text = "▲",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun ExpandableSection(ItemsList: List<BudgetItem>) {
    var isOpen by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = Modifier
    ) {
        ExpandableHeader("Expenses", { isOpen = !isOpen }, isOpen)
        if (isOpen) {
            BudgetList(ItemsList)
        }
    }

}

@Composable
fun ListItem(budgetItem: BudgetItem) {
    Card(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(6.dp),
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
                text = amountToCurrency(budgetItem.amount),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                //.fillMaxWidth()
            )
        }
    }
}

@Composable
fun BudgetList(
    budgetItemList: List<BudgetItem>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(budgetItemList.size) { index ->
            ListItem(budgetItemList[index])
        }
    }
}

@Composable
fun BudgetListScreen() {
    ExpandableSection(tempBudgetItems)
}

@Preview(showBackground = true)
@Composable
fun ListPreview() {
    BudgetApp2Theme {
        BudgetListScreen()
    }
}
