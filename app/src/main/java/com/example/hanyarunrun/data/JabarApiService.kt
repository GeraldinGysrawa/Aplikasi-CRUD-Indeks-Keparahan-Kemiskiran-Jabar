package com.example.hanyarunrun.data

import retrofit2.Response
import retrofit2.http.GET

interface JabarApiService {
    @GET("bigdata/bps/od_19998_indeks_keparahan_kemiskinan__kabupatenkota")
    suspend fun getIndeksKemiskinan(): Response<OpenDataJabarResponse>
}
