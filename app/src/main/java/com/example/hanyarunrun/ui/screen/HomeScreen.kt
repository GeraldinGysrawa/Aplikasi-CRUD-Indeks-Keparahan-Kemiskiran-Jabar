package com.example.hanyarunrun.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.hanyarunrun.viewmodel.DataViewModel
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.navigation.NavHostController


@Composable
fun HomeScreen(viewModel: DataViewModel) {
    val totalData by viewModel.totalData.observeAsState(0)

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Total Data: $totalData",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}
