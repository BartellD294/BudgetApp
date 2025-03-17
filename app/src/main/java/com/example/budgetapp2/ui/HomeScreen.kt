package com.example.budgetapp2.ui

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = viewModel(factory = ViewModelProvider.Factory)) {
    val homeUiState by homeViewModel.homeUiState.collectAsState()

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
            items(homeUiState.budgetItemList.size) {
                Card(
                    modifier = Modifier.padding(1.dp)
                ) {
                    Text(text = homeUiState.budgetItemList[it].name)
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}