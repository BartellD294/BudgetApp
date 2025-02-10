package com.example.budgetapp2.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    BudgetList(tempBudgetItems)
}

@Preview(showBackground = true)
@Composable
fun ListPreview() {
    BudgetApp2Theme {
        BudgetListScreen()
    }
}
