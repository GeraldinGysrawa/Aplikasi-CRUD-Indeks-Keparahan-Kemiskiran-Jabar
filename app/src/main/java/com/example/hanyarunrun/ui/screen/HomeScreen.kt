package com.example.hanyarunrun.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hanyarunrun.viewmodel.DataViewModel

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet

import com.github.mikephil.charting.components.XAxis

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun HomeScreen(viewModel: DataViewModel) {
    val totalData by viewModel.totalData.observeAsState(0)
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Total Data", "Grafik")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Home",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Divider(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.onBackground,
                thickness = 1.dp
            )
        }

        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }

        when (selectedTabIndex) {
            0 -> TotalDataScreen(totalData)
            1 -> GrafikScreen(viewModel)
        }
    }
}

@Composable
fun TotalDataScreen(totalData: Int) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Total Data",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Data by OpenDataJabar",
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$totalData",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun GrafikScreen(viewModel: DataViewModel = viewModel()) {
    val dataPoints by viewModel.graphData.observeAsState(emptyList())

    if (dataPoints.isEmpty()) {
        Text(text = "Tidak ada data", modifier = Modifier.padding(16.dp))
    } else {
        AndroidView(
            factory = { context ->
                BarChart(context).apply {
                    val entries = dataPoints.mapIndexed { index, (tahun, jumlah) ->
                        BarEntry(index.toFloat(), jumlah.toFloat())
                    }

                    val labels = dataPoints.map { it.tahun.toString() } // Tahun sebagai label

                    val dataSet = BarDataSet(entries, "Data per Tahun").apply {
                        val BLUE = Color(0xFF0000FF)
                        valueTextSize = 12f
                    }

                    val barData = BarData(dataSet)
                    this.data = barData

                    // Atur sumbu X untuk menampilkan tahun
                    this.xAxis.apply {
                        valueFormatter = IndexAxisValueFormatter(labels)
                        granularity = 1f
                        position = XAxis.XAxisPosition.BOTTOM
                    }

                    this.invalidate() // Refresh chart
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(16.dp)
        )
    }
}

