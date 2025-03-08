package com.example.hanyarunrun.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.hanyarunrun.data.DataEntity
import com.example.hanyarunrun.viewmodel.DataViewModel

@Composable
fun EditScreen(
    navController: NavHostController,
    viewModel: DataViewModel,
    dataId: Int
) {
    val context = LocalContext.current

    var kodeProvinsi by remember { mutableStateOf(0) }
    var namaProvinsi by remember { mutableStateOf("") }
    var kodeKabupatenKota by remember { mutableStateOf(0) }
    var namaKabupatenKota by remember { mutableStateOf("") }
    var indeksKeparahanKemiskinan by remember { mutableStateOf("") }
    var satuan by remember { mutableStateOf("") }
    var tahun by remember { mutableStateOf("") }

    LaunchedEffect(dataId) {
        viewModel.getDataById(dataId)?.let { data ->
            kodeProvinsi = data.kodeProvinsi
            namaProvinsi = data.namaProvinsi
            kodeKabupatenKota = data.kodeKabupatenKota
            namaKabupatenKota = data.namaKabupatenKota
            indeksKeparahanKemiskinan = data.indeksKeparahanKemiskinan.toString()
            satuan = data.satuan
            tahun = data.tahun.toString()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Edit Data",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            // Input Kode Provinsi
            OutlinedTextField(
                value = if (kodeProvinsi == 0) "" else kodeProvinsi.toString(),
                onValueChange = { kodeProvinsi = it.toIntOrNull() ?: 0 },
                label = { Text("Kode Provinsi") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // Input Nama Provinsi
            OutlinedTextField(
                value = namaProvinsi,
                onValueChange = { namaProvinsi = it },
                label = { Text("Nama Provinsi") },
                modifier = Modifier.fillMaxWidth()
            )

            // Input Kode Kabupaten/Kota
            OutlinedTextField(
                value = if (kodeKabupatenKota == 0) "" else kodeKabupatenKota.toString(),
                onValueChange = { kodeKabupatenKota = it.toIntOrNull() ?: 0 },
                label = { Text("Kode Kabupaten/Kota") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // Input Nama Kabupaten/Kota
            OutlinedTextField(
                value = namaKabupatenKota,
                onValueChange = { namaKabupatenKota = it },
                label = { Text("Nama Kabupaten/Kota") },
                modifier = Modifier.fillMaxWidth()
            )

            // Input Indeks Keparahan Kemiskinan
            OutlinedTextField(
                value = indeksKeparahanKemiskinan,
                onValueChange = { indeksKeparahanKemiskinan = it },
                label = { Text("Indeks Keparahan Kemiskinan") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // Input Satuan
            OutlinedTextField(
                value = satuan,
                onValueChange = { satuan = it },
                label = { Text("Satuan") },
                modifier = Modifier.fillMaxWidth()
            )

            // Input Tahun
            OutlinedTextField(
                value = tahun,
                onValueChange = { tahun = it },
                label = { Text("Tahun") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Tombol Update
            Button(
                onClick = {
                    val updatedData = DataEntity(
                        id = dataId,
                        kodeProvinsi = kodeProvinsi,
                        namaProvinsi = namaProvinsi,
                        kodeKabupatenKota = kodeKabupatenKota,
                        namaKabupatenKota = namaKabupatenKota,
                        indeksKeparahanKemiskinan = indeksKeparahanKemiskinan.toDoubleOrNull() ?: 0.0,
                        satuan = satuan,
                        tahun = tahun.toIntOrNull() ?: 0
                    )
                    viewModel.updateData(updatedData)
                    Toast.makeText(context, "Data berhasil diupdate!", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Update Data")
            }
        }
    }
}
