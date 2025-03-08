package com.example.hanyarunrun.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.hanyarunrun.data.AppDatabase
import com.example.hanyarunrun.data.DataEntity
import com.example.hanyarunrun.data.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import com.example.hanyarunrun.data.YearlyData
import com.example.hanyarunrun.data.DataDao


class DataViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).dataDao()

    // LiveData langsung dari database agar selalu up-to-date
    val dataList: LiveData<List<DataEntity>> = dao.getAll()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    val totalData: LiveData<Int> = dao.getTotalCount()

    fun fetchDataFromApi() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = withContext(Dispatchers.IO) { RetrofitClient.api.getIndeksKemiskinan() }
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.error == 0) {
                        withContext(Dispatchers.IO) {
                            dao.deleteAll()
                            val entities = responseBody.data.map { data ->
                                DataEntity(
                                    kodeProvinsi = data.kode_provinsi,
                                    namaProvinsi = data.nama_provinsi,
                                    kodeKabupatenKota = data.kode_kabupaten_kota,
                                    namaKabupatenKota = data.nama_kabupaten_kota,
                                    indeksKeparahanKemiskinan = data.indeks_keparahan_kemiskinan.toDouble(),
                                    satuan = data.satuan,
                                    tahun = data.tahun
                                )
                            }
                            dao.insertAll(entities)
                        }
                    } else {
                        _errorMessage.postValue("Error dari API: ${responseBody?.message}")
                    }
                } else {
                    _errorMessage.postValue("Gagal mengambil data: ${response.message()}")
                }
            } catch (e: Exception) {
                _errorMessage.postValue("Terjadi kesalahan: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun insertData(
        kodeProvinsi: Int,
        namaProvinsi: String,
        kodeKabupatenKota: Int,
        namaKabupatenKota: String,
        indeksKeparahanKemiskinan: String,
        satuan: String,
        tahun: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val indeksValue = indeksKeparahanKemiskinan.toDoubleOrNull() ?: 0.0
            val tahunValue = tahun.toIntOrNull() ?: Calendar.getInstance().get(Calendar.YEAR)
            val entity = DataEntity(
                kodeProvinsi = kodeProvinsi,
                namaProvinsi = namaProvinsi,
                kodeKabupatenKota = kodeKabupatenKota,
                namaKabupatenKota = namaKabupatenKota,
                indeksKeparahanKemiskinan = indeksValue,
                satuan = satuan,
                tahun = tahunValue
            )
            dao.insert(entity)
        }
    }

    fun updateData(data: DataEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.update(data)
        }
    }

    fun deleteData(data: DataEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.delete(data)
        }
    }

    fun deleteDataById(dataId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteById(dataId)
        }
    }

    suspend fun getDataById(id: Int): DataEntity? {
        return withContext(Dispatchers.IO) {
            dao.getById(id)
        }
    }

    private val dataDao: DataDao = AppDatabase.getDatabase(application).dataDao()

    // Ambil data untuk grafik langsung dari DAO
    val graphData: LiveData<List<YearlyData>> = dataDao.getDataByYear().asLiveData()

    // Statistik indeks kemiskinan (tanpa pengelompokan per tahun)
    val averageIndex: LiveData<Double> = dao.getAll().map { list ->
        list.map { it.indeksKeparahanKemiskinan }.average()
    }

    val minIndex: LiveData<Double> = dao.getAll().map { list ->
        list.minOfOrNull { it.indeksKeparahanKemiskinan } ?: 0.0
    }

    val maxIndex: LiveData<Double> = dao.getAll().map { list ->
        list.maxOfOrNull { it.indeksKeparahanKemiskinan } ?: 0.0
    }
}

