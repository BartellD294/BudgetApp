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
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.VerticalDivider
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.budgetapp2.data.valueToCurrency

val barGraphWidth = 50
val barGraphMaxHeight = 300

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(factory = ViewModelProvider.Factory)
) {
    val homeUiState by viewModel.homeUiState.collectAsState()

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
    ) {
        HorizontalDivider(
            modifier = Modifier//.weight(1f)
                .padding(4.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            thickness = 3.dp
        )
        Card(
            modifier = Modifier.padding(4.dp)
                .fillMaxWidth()
        ) {
            Column{
                Text(
                    text = "Weekly Income: " + valueToCurrency(homeUiState.totalIncomesDailyValue * 7.0),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(horizontal = 4.dp,
                        vertical = 8.dp)
                        .align(Alignment.Start)
                )
                Text(
                    text = "Weekly Expenses: " + valueToCurrency(homeUiState.totalExpensesDailyValue * 7.0),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(horizontal = 4.dp,
                        vertical = 8.dp)
                        .align(Alignment.Start)
                )
            }

        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            HorizontalDivider(
                modifier = Modifier.weight(1f)
                    .padding(4.dp),
                thickness = 3.dp
            )
            Text(
                modifier = Modifier.padding(4.dp),
                text = "Weekly Expenses Breakdown",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
            )
            HorizontalDivider(
                modifier = Modifier.weight(1f)
                    .padding(4.dp),
                thickness = 3.dp
            )
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
            CategoryPieChart(homeUiState)
        }

    }
}

@Composable
fun ItemBarGraph(
    homeUiState: HomeUiState,
) {
    Row{
        BarGraphVerticalScale(homeUiState, 0)
        LazyRow(
            verticalAlignment = Alignment.Bottom,
        ) {
            items(homeUiState.expenseList.size) { index ->
                //val thisHeight = ((homeUiState.expenseList[index].value / homeUiState.maxItemValue) * barGraphMaxHeight)
                val thisHeight = ((homeUiState.expenseList[index].valuePerDay / homeUiState.maxExpenseDailyValue) * barGraphMaxHeight)
                Column{
                    Box(modifier = Modifier
                        .size(
                            width = (barGraphWidth).dp,
                            height = thisHeight.dp
                        )
                        .padding(4.dp)
                        .background(colors[index % colors.size])
                    ) {

                    }
                    Text(
                        text = "[" + homeUiState.expenseList[index].id.toString() + "]",
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        //textAlign = androidx.compose.ui.text.style.TextAlign.End
                    )
                }

            }
        }
    }

    Column(modifier = Modifier,
    //LazyColumn(modifier = Modifier,
        //userScrollEnabled = false
    ) {
        for (index: Int in 0 until homeUiState.expenseList.size) {
        //items(homeUiState.expenseList.size) { index ->
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                ) {
                    Text(
                        text = "■",
                        color = colors[homeUiState.expenseList.indexOf(homeUiState.expenseList[index]) % colors.size]
                    )
                    Text(text = "[" + homeUiState.expenseList[index].id.toString() + "]")
                }
                Text(text = homeUiState.expenseList[index].name)
                //if (homeUiState.totalOrWeekly == 0) {
                //    Text(text = homeUiState.expenseList[index].value.toString())
                //} else {
                    Text(
                        text = (valueToCurrency(homeUiState.expenseList[index].valuePerDay * 7.0))
                    )
                //}

            }
        }
    }
}

@Composable
fun BarGraphVerticalScale(
    homeUiState: HomeUiState,
    itemsOrCategories: Int
) {
    Column(
        modifier = Modifier.height(barGraphMaxHeight.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HorizontalDivider(
            modifier = Modifier//.weight(1f)
                .width(barGraphWidth.dp),
            thickness = 3.dp
        )
        Text(
            text = if (itemsOrCategories == 0) {
                valueToCurrency(homeUiState.maxExpenseDailyValue * 7.0)
            } else {
                valueToCurrency(homeUiState.maxTotalDailyCategoryValue * 7.0)
            }
        )
        VerticalDivider(
            modifier = Modifier.weight(1f),
            //.height(barGraphMaxHeight.dp),
            thickness = 3.dp
        )
        Text(
            text = "$0.00"
        )
        HorizontalDivider(
            modifier = Modifier//.weight(1f)
                .width(barGraphWidth.dp),
            thickness = 3.dp
        )
    }
}

@Composable
fun CategoryBarGraph(
    homeUiState: HomeUiState
) {
    Row {
        BarGraphVerticalScale(homeUiState, 1)
        LazyRow(
            verticalAlignment = Alignment.Bottom,
        ) {

            items(homeUiState.expenseCategoryList.size) { index ->
                //val thisHeight = ((homeUiState.expenseCategoryList[index].totalValue / homeUiState.maxTotalCategoryValue) * barGraphMaxHeight)
                val thisHeight = ((homeUiState.expenseCategoryList[index].totalValuePerDay / homeUiState.maxTotalDailyCategoryValue) * barGraphMaxHeight)
                Column{
                    Box(modifier = Modifier
                        .size(
                            width = (barGraphWidth).dp,
                            height = thisHeight.dp
                        )
                        .padding(4.dp)
                        .background(colors[index % colors.size])
                    ) {
                        //Text(text = "[" + homeUiState.categoryList[index].toString() + "]",
                    }
                    Text(
                        text = "[" + (index+1).toString() + "]",
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        //textAlign = androidx.compose.ui.text.style.TextAlign.End
                    )
                }

            }
        }
    }

    //LazyColumn(modifier = Modifier) {
    Column(modifier = Modifier) {
        for (index: Int in 0 until homeUiState.expenseCategoryList.size) {
        //items(homeUiState.expenseCategoryList.size) { index ->
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                ) {
                    Text(
                        text = "■",
                        color = colors[homeUiState.expenseCategoryList.indexOf(homeUiState.expenseCategoryList[index]) % colors.size]
                    )
                    Text(text = "[" + (index + 1).toString() + "]")
                }
                Text(text = homeUiState.expenseCategoryList[index].name)
                //Text(text = homeUiState.expenseCategoryList[index].totalValue.toString())
                Text(text = valueToCurrency(homeUiState.expenseCategoryList[index].totalValuePerDay * 7.0))
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
            for (item in homeUiState.expenseList) {
                itemFraction = ((item.valuePerDay / homeUiState.totalExpensesDailyValue) * 360.0)
                drawArc(
                    color = colors[homeUiState.expenseList.indexOf(item) % colors.size],
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
        Column(modifier = Modifier) {
        //LazyColumn(modifier = Modifier) {
            for (index: Int in 0 until homeUiState.expenseList.size) {
            //items(homeUiState.expenseList.size) { index ->
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        Text(
                            text = "■",
                            color = colors[homeUiState.expenseList.indexOf(homeUiState.expenseList[index]) % colors.size]
                        )
                        Text(text = "[" + homeUiState.expenseList[index].id.toString() + "]")
                    }
                    Text(text = homeUiState.expenseList[index].name)
                    Text(text = valueToCurrency(homeUiState.expenseList[index].valuePerDay * 7.0))
                }
            }
        }
    }
}

@Composable
fun CategoryPieChart(homeUiState: HomeUiState) {
    var categoryIndex = 0
    var categoryStartAngle = 0.0
    var categoryFraction = 0.0
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
            for (cat in homeUiState.expenseCategoryList) {
                categoryFraction = ((cat.totalValuePerDay / homeUiState.totalExpensesDailyValue) * 360.0)
                drawArc(
                    color = colors[homeUiState.expenseCategoryList.indexOf(cat) % colors.size],
                    startAngle = categoryStartAngle.toFloat(),
                    sweepAngle = categoryFraction.toFloat(),
                    useCenter = true,
                    size = Size(size.width, size.height),
                    topLeft = Offset(0f, 0f)
                )
                categoryIndex += 1
                categoryStartAngle += categoryFraction
            }
        }
        Column(modifier = Modifier) {
        //LazyColumn(modifier = Modifier) {
            for (index: Int in 0 until homeUiState.expenseCategoryList.size) {
            //items(homeUiState.expenseCategoryList.size) { index ->
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        Text(
                            text = "■",
                            color = colors[homeUiState.expenseCategoryList.indexOf(homeUiState.expenseCategoryList[index]) % colors.size]
                        )
                        Text(text = "[" + (index + 1).toString() + "]")
                    }

                    Text(text = homeUiState.expenseCategoryList[index].name)
                    Text(text = valueToCurrency(homeUiState.expenseCategoryList[index].totalValuePerDay * 7.0))
                }
            }
        }
    }
}

val saturatedColors = listOf(
    Color(0xFFFF4040),
    Color(0xFFFFA040),
    Color(0xFFffff40),
    Color(0xFFA0FF40),
    Color(0xFF40ff40),
    Color(0xFF40FFA0),
    Color(0xFF40FFFF),
    Color(0xFF40A0FF),
    Color(0xFF4040FF),
    Color(0xFFA040FF),
    Color(0xFFFF40FF),
    Color(0xFFFF40A0),
)

val colors = listOf(
    Color(0xFFFF8080),
    Color(0xFFFFC080),
    Color(0xFFFFFF80),
    Color(0xFFC0FF80),
    Color(0xFF80FF80),
    Color(0xFF80FFC0),
    Color(0xFF80FFFF),
    Color(0xFF80C0FF),
    Color(0xFF8080FF),
    Color(0xFFC080FF),
    Color(0xFFFF80FF),
    Color(0xFFFF80C0),
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