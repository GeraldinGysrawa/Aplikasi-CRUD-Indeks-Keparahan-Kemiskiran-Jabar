package com.example.hanyarunrun.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.hanyarunrun.viewmodel.DataViewModel

@Composable
fun DataEntryScreen(navController: NavHostController, viewModel: DataViewModel) {
    val context = LocalContext.current

    // State untuk input data
    var kodeProvinsi by remember { mutableStateOf("") }
    var namaProvinsi by remember { mutableStateOf("") }
    var kodeKabupatenKota by remember { mutableStateOf("") }
    var namaKabupatenKota by remember { mutableStateOf("") }
    var indeksKeparahanKemiskinan by remember { mutableStateOf("") }
    var satuan by remember { mutableStateOf("") }
    var tahun by remember { mutableStateOf("") }

    // State untuk validasi error
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Input Data",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineMedium
            )

            // Input Kode Provinsi (Harus Angka)
            OutlinedTextField(
                value = kodeProvinsi,
                onValueChange = { kodeProvinsi = it },
                label = { Text("Kode Provinsi") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                isError = isError && kodeProvinsi.toIntOrNull() == null,
                supportingText = {
                    if (isError && kodeProvinsi.toIntOrNull() == null) Text("Kode Provinsi harus berupa angka")
                }
            )

            // Input Nama Provinsi
            OutlinedTextField(
                value = namaProvinsi,
                onValueChange = { namaProvinsi = it },
                label = { Text("Nama Provinsi") },
                modifier = Modifier.fillMaxWidth(),
                isError = isError && namaProvinsi.isBlank(),
                supportingText = {
                    if (isError && namaProvinsi.isBlank()) Text("Nama Provinsi tidak boleh kosong")
                }
            )

            // Input Kode Kabupaten/Kota (Harus Angka)
            OutlinedTextField(
                value = kodeKabupatenKota,
                onValueChange = { kodeKabupatenKota = it },
                label = { Text("Kode Kabupaten/Kota") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                isError = isError && kodeKabupatenKota.toIntOrNull() == null,
                supportingText = {
                    if (isError && kodeKabupatenKota.toIntOrNull() == null) Text("Kode Kabupaten/Kota harus berupa angka")
                }
            )

            // Input Nama Kabupaten/Kota
            OutlinedTextField(
                value = namaKabupatenKota,
                onValueChange = { namaKabupatenKota = it },
                label = { Text("Nama Kabupaten/Kota") },
                modifier = Modifier.fillMaxWidth(),
                isError = isError && namaKabupatenKota.isBlank(),
                supportingText = {
                    if (isError && namaKabupatenKota.isBlank()) Text("Nama Kabupaten/Kota tidak boleh kosong")
                }
            )

            // Input Indeks Keparahan Kemiskinan (harus angka valid)
            OutlinedTextField(
                value = indeksKeparahanKemiskinan,
                onValueChange = { indeksKeparahanKemiskinan = it },
                label = { Text("Indeks Keparahan Kemiskinan") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                isError = isError && indeksKeparahanKemiskinan.toDoubleOrNull() == null,
                supportingText = {
                    if (isError && indeksKeparahanKemiskinan.toDoubleOrNull() == null)
                        Text("Masukkan angka yang valid")
                }
            )

            // Input Satuan
            OutlinedTextField(
                value = satuan,
                onValueChange = { satuan = it },
                label = { Text("Satuan") },
                modifier = Modifier.fillMaxWidth(),
                isError = isError && satuan.isBlank(),
                supportingText = {
                    if (isError && satuan.isBlank()) Text("Satuan tidak boleh kosong")
                }
            )

            // Input Tahun (harus angka dan >= 2000)
            OutlinedTextField(
                value = tahun,
                onValueChange = { tahun = it },
                label = { Text("Tahun") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                isError = isError && (tahun.toIntOrNull() == null || tahun.toInt() < 2000),
                supportingText = {
                    if (isError && tahun.toIntOrNull() == null) {
                        Text("Masukkan tahun yang valid")
                    } else if (isError && tahun.toInt() < 2000) {
                        Text("Tahun tidak boleh kurang dari 2000")
                    }
                }
            )

            // Tombol Submit
            Button(
                onClick = {
                    // Cek validasi sebelum submit
                    isError = false
                    errorMessage = ""

                    val kodeProvinsiInt = kodeProvinsi.toIntOrNull()
                    val kodeKabupatenKotaInt = kodeKabupatenKota.toIntOrNull()
                    val tahunInt = tahun.toIntOrNull()
                    val indeksDouble = indeksKeparahanKemiskinan.toDoubleOrNull()

                    if (kodeProvinsiInt == null || kodeKabupatenKotaInt == null ||
                        namaProvinsi.isBlank() || namaKabupatenKota.isBlank() ||
                        indeksDouble == null || satuan.isBlank() ||
                        tahunInt == null || tahunInt < 2000
                    ) {
                        isError = true
                        errorMessage = "Pastikan semua input valid!"
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    } else {
                        // Jika valid, panggil ViewModel untuk menyimpan data
                        viewModel.insertData(
                            kodeProvinsi = kodeProvinsiInt,
                            namaProvinsi = namaProvinsi,
                            kodeKabupatenKota = kodeKabupatenKotaInt,
                            namaKabupatenKota = namaKabupatenKota,
                            indeksKeparahanKemiskinan = indeksDouble.toString(),
                            satuan = satuan,
                            tahun = tahunInt.toString()
                        )
                        Toast.makeText(context, "Data berhasil ditambahkan!", Toast.LENGTH_SHORT).show()

                        // Navigasi ke tampilan daftar data
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submit Data")
            }
        }
    }
}
