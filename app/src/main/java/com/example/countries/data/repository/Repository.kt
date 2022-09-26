package com.example.countries.data.repository

import android.util.Log
import com.example.countries.data.model.DetailResponseModel
import com.example.countries.data.model.ResponseModel
import com.example.countries.data.network.ApiInterface
import com.example.countries.util.NetworkResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class Repository @Inject constructor( private val service: ApiInterface) {

    suspend fun getAllCountries(limit: String)
            : Flow<NetworkResponse<ResponseModel>> =
        flow {
            try {
                emit(NetworkResponse.Loading())
                val result = service.getAllCountries(limit)
                if (result.isSuccessful) {
                    val countriesResponse = result.body()
                    Log.d("Result Country: ", countriesResponse.toString())
                    emit(NetworkResponse.Success(countriesResponse))
                } else {
                    emit(NetworkResponse.Error("Result error"))
                }
            } catch (e: Exception) {
                Log.d("Error: ", e.message.toString())
                emit(NetworkResponse.Error(e.message))
            }
        }


    suspend fun getCountryDetails(countryCode: String): Flow<NetworkResponse<DetailResponseModel>> =
        flow {
            try {
                emit(NetworkResponse.Loading())
                val result = service.getCountryDetail(countryCode)
                if (result.isSuccessful) {
                    val countryDetailResponse = result.body()
                    Log.d("Detail Result Country: ", countryDetailResponse.toString())
                    emit(NetworkResponse.Success(countryDetailResponse))

                } else {
                    emit(NetworkResponse.Error("Result error"))
                }

            } catch (e: Exception) {
                Log.d("Error: ", e.message.toString())
                emit(NetworkResponse.Error(e.message))
            }
        }


}