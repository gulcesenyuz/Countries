package com.example.countries.data.network

import com.example.countries.data.model.DetailResponseModel
import com.example.countries.data.model.ResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {
    @GET("countries")
    suspend fun getAllCountries(
        @Query("limit") limit: String
    ): Response<ResponseModel>

    @GET("countries/{countryCode}")
    suspend fun getCountryDetail(
        @Path("countryCode") countryCode: String,
    ): Response<DetailResponseModel>
}