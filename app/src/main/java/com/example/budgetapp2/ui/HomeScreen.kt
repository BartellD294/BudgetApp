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
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
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

    Column(
        //horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            SegmentedButton(
                selected = homeUiState.barOrPie == 0,
                onClick = { viewModel.updateBarOrPie(0) },
                shape = SegmentedButtonDefaults.itemShape(
                    index = 0,
                    count = 2
                ),
                label = { Text(text = "Bar Graph") },
            )
            SegmentedButton(
                selected = homeUiState.barOrPie == 1,
                onClick = { viewModel.updateBarOrPie(1) },
                shape = SegmentedButtonDefaults.itemShape(
                    index = 1,
                    count = 2
                ),
                label = { Text(text = "Pie Chart") },
            )
        }
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            SegmentedButton(
                selected = homeUiState.itemsOrCategories == 0,
                onClick = { viewModel.updateItemsOrCategories(0) },
                shape = SegmentedButtonDefaults.itemShape(
                    index = 0,
                    count = 2
                ),
                label = { Text(text = "Items") },
            )
            SegmentedButton(
                selected = homeUiState.itemsOrCategories == 1,
                onClick = { viewModel.updateItemsOrCategories(1) },
                shape = SegmentedButtonDefaults.itemShape(
                    index = 1,
                    count = 2
                ),
                label = { Text(text = "Categories") },
            )
        }

        if (homeUiState.barOrPie == 0 && homeUiState.itemsOrCategories == 0) {
            ItemBarGraph(homeUiState)
        }
        else if (homeUiState.barOrPie == 0 && homeUiState.itemsOrCategories == 1) {
            CategoryBarGraph(homeUiState)
        }
        else if (homeUiState.barOrPie == 1 && homeUiState.itemsOrCategories == 0) {
            ItemPieChart(homeUiState)
        }
        else if (homeUiState.barOrPie == 1 && homeUiState.itemsOrCategories == 1) {
            //PieChart(homeUiState)
        }

    }
}

@Composable
fun ItemBarGraph(
    homeUiState: HomeUiState,
) {
    LazyRow(
        verticalAlignment = Alignment.Bottom,
    ) {
        items(homeUiState.budgetItemList.size) { index ->
            val thisHeight = ((homeUiState.budgetItemList[index].value / homeUiState.maxItemValue) * barGraphMaxHeight)
            Box(modifier = Modifier
                .size(
                    width = (barGraphWidth).dp,
                    height = thisHeight.dp
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
                Text(text = homeUiState.budgetItemList[index].value.toString())
            }
        }
    }
}

@Composable
fun CategoryBarGraph(
    homeUiState: HomeUiState
) {
    LazyRow(
        verticalAlignment = Alignment.Bottom,
    ) {
        items(homeUiState.categoryList.size) { index ->
            val thisHeight = ((homeUiState.categoryList[index].totalValue / homeUiState.maxTotalCategoryValue) * barGraphMaxHeight)
            Box(modifier = Modifier
                .size(
                    width = (barGraphWidth).dp,
                    height = thisHeight.dp
                )
                .padding(4.dp)
                .background(Color.LightGray)
            ) {
                //Text(text = "[" + homeUiState.categoryList[index].toString() + "]",
                Text(text = "[" + (index+1).toString() + "]",
                    modifier = Modifier.align(Alignment.BottomCenter),
                    textAlign = androidx.compose.ui.text.style.TextAlign.End)
            }
        }
    }
    LazyColumn(modifier = Modifier) {
        items(homeUiState.categoryList.size) { index ->
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,) {
                Text(text = "[" + (index+1).toString() + "]")
                Text(text = homeUiState.categoryList[index].name)
                Text(text = homeUiState.categoryList[index].totalValue.toString())
            }
        }
    }
}

@Composable
fun ItemPieChart(homeUiState: HomeUiState) {
    var itemIndex = 0
    var itemStartAngle = 0.0
    var itemFraction = 0.0
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(30.dp)
        ) {
            for (item in homeUiState.budgetItemList) {
                itemFraction = ((item.value / homeUiState.totalExpensesValue) * 360.0)
                drawArc(
                    color = colors[homeUiState.budgetItemList.indexOf(item) % colors.size],
                    startAngle = itemStartAngle.toFloat(),
                    sweepAngle = itemFraction.toFloat(),
                    useCenter = true,
                    size = Size(size.width, size.height),
                    topLeft = Offset(0f, 0f)
                )
                itemIndex += 1
                itemStartAngle += itemFraction
            }
        }
        LazyColumn(modifier = Modifier) {
            items(homeUiState.budgetItemList.size) { index ->
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,) {
                    Text(text = "[" + homeUiState.budgetItemList[index].id.toString() + "]",
                        color = colors[homeUiState.budgetItemList.indexOf(homeUiState.budgetItemList[index]) % colors.size])
                    Text(text = homeUiState.budgetItemList[index].name)
                    Text(text = homeUiState.budgetItemList[index].value.toString())
                }
            }
        }
    }

}
val colors = listOf(
    Color(0xFFF44336),
    Color(0xFFE91E63),
    Color(0xFF9C27B0),
    Color(0xFF673AB7),
    Color(0xFF3F51B5),
    Color(0xFF2196F3),
    Color(0xFF03A9F4),
    Color(0xFF00BCD4),
    Color(0xFF009688),
    Color(0xFF4CAF50),
    Color(0xFF8BC34A),
    Color(0xFFCDDC39),
    Color(0xFFFFEB3B),
    Color(0xFFFFC107),
    Color(0xFFFF9800),
    Color(0xFFFF5722),
    Color(0xFF795548),
)

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