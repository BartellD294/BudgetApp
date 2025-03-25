package com.example.budgetapp2.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.size
import androidx.compose.ui.unit.times

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
        LazyRow {
            val maxBarHeight = 50.0
            items(homeUiState.budgetItemList.size) {
                Box(modifier = Modifier
                    .size(width = 10.dp, height = (50).dp)
                    .padding(4.dp)
                    .background(Color.Blue)
                )
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