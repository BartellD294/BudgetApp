package com.example.budgetapp2.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

val barGraphWidth = 50
val barGraphMaxHeight = 300

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(factory = ViewModelProvider.Factory)
) {
    val homeUiState by viewModel.homeUiState.collectAsState()

    Column() {
        Card(
            modifier = Modifier.padding(4.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Home header",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 4.dp,
                    vertical = 8.dp)
                    .align(Alignment.CenterHorizontally))
        }
        SingleChoiceSegmentedButtonRow {
            SegmentedButton(
                selected = homeUiState.buttonIndex == 0,
                onClick = { viewModel.updateButton(0) },
                shape = SegmentedButtonDefaults.itemShape(
                    index = 0,
                    count = 2
                ),
                label = { Text(text = "Bar Graph: Items") },
            )
            SegmentedButton(
                selected = homeUiState.buttonIndex == 1,
                onClick = { viewModel.updateButton(1)
                          Log.i("button index", homeUiState.buttonIndex.toString()) },
                shape = SegmentedButtonDefaults.itemShape(
                    index = 1,
                    count = 2
                ),
                label = { Text(text = "Bar Graph: Categories") },
            )
        }
        if (homeUiState.buttonIndex == 0) {
            ItemBarGraph(homeUiState)
        }
        else if (homeUiState.buttonIndex == 1) {
            CategoryBarGraph(homeUiState)
        }

    }
}

@Composable
fun ItemBarGraph(
    homeUiState: HomeUiState
) {

    LazyRow {
        //Log.i("Max cost", homeUiState.maxCost.toString())
        Log.i("Item 1 cost", homeUiState.budgetItemList.toString())
        //Log.i("Item 1 height", ((homeUiState.budgetItemList[0].cost / homeUiState.maxCost) * barGraphMaxHeight).toString())
        items(homeUiState.budgetItemList.size) { index ->
            Box(modifier = Modifier
                .size(
                    width = (barGraphWidth).dp,
                    height = ((homeUiState.budgetItemList[index].cost / homeUiState.maxCost) * barGraphMaxHeight).dp
                )
                .padding(4.dp)
                .background(Color.LightGray)
            ) {
                Text(text = "[" + homeUiState.budgetItemList[index].id.toString() + "]",
                    modifier = Modifier.align(Alignment.BottomCenter),
                    textAlign = androidx.compose.ui.text.style.TextAlign.End)
            }
        }
    }
    LazyColumn(modifier = Modifier) {
        items(homeUiState.budgetItemList.size) { index ->
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,) {
                Text(text = "[" + homeUiState.budgetItemList[index].id.toString() + "]")
                Text(text = homeUiState.budgetItemList[index].name)
                Text(text = homeUiState.budgetItemList[index].cost.toString())
            }
        }
    }
}

@Composable
fun CategoryBarGraph(
    homeUiState: HomeUiState
) {
    LazyRow {
        //Log.i("Max cost", homeUiState.maxCost.toString())
        Log.i("Item 1 cost", homeUiState.budgetItemList.toString())
        //Log.i("Item 1 height", ((homeUiState.budgetItemList[0].cost / homeUiState.maxCost) * barGraphMaxHeight).toString())
        items(homeUiState.categoryList.size) { index ->
            Box(modifier = Modifier
                .size(
                    width = (barGraphWidth).dp,
                    height = ((homeUiState.categoryList[index].totalCost / homeUiState.maxCategoryTotal) * barGraphMaxHeight).dp
                )
                .padding(4.dp)
                .background(Color.LightGray)
            ) {
                Text(text = "[" + homeUiState.categoryList[index].toString() + "]",
                    modifier = Modifier.align(Alignment.BottomCenter),
                    textAlign = androidx.compose.ui.text.style.TextAlign.End)
            }
        }
    }
    LazyColumn(modifier = Modifier) {
        items(homeUiState.categoryList.size) { index ->
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,) {
                //Text(text = "[" + homeUiState.categoryList[index].name.to + "]")
                Text(text = homeUiState.categoryList[index].name)
                Text(text = homeUiState.categoryList[index].totalCost.toString())
            }
        }
    }
}

/*

fun incrementColors(num_elements: Int): List<Color> {
    val colors = mutableListOf<Color>()
    for (i in 0 until num_elements) {
        val index = i/num_elements

        val r = index *
        colors.add(calculateColor(i, i, i))

    }
}


r = 512
g = 0           0
b = 0

r = 255
g = 255         1
b = -255

r = 0
g = 512         2
b = 0

r = -255
g = 255         3
b = 255

r = 0
g = 0           4
b = 512

r = 255
g = -255        5
b = 255
*/

fun calculateColor(r: Int, g: Int, b: Int): Color {
    return Color(r, g, b)
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}