package com.app.parkfinder.logic.services

import com.app.parkfinder.logic.models.NominatimResult
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NominatimService {
    @GET("search")
    fun getCoordinates(
        @Query("q") query: String,
        @Query("format") format: String = "json",
        @Query("limit") limit: Int = 1,
        @Query("addressdetails") addressdetails: Int = 1,
        @Header("User-Agent") userAgent: String
    ): Call<List<NominatimResult>>

    companion object {
        fun create(): NominatimService {
            return Retrofit.Builder()
                .baseUrl("https://nominatim.openstreetmap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NominatimService::class.java)
        }
    }
}