package com.example.hanyarunrun.data

data class OpenDataJabarResponse(
    val data: List<IndeksKemiskinanData>,
    val error: Int,
    val message: String
)

data class IndeksKemiskinanData(
    val id: Int,
    val indeks_keparahan_kemiskinan: Float,
    val kode_kabupaten_kota: Int,
    val kode_provinsi: Int,
    val nama_kabupaten_kota: String,
    val nama_provinsi: String,
    val satuan: String,
    val tahun: Int
)
