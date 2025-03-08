package com.example.hanyarunrun.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hanyarunrun.viewmodel.DataViewModel

@Composable
fun HomeScreen(viewModel: DataViewModel) {
    val totalData by viewModel.totalData.observeAsState(0)

    Column(modifier = Modifier.padding(16.dp)) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Total Data: $totalData",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    }
}
