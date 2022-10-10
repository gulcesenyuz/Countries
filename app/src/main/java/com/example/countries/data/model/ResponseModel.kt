package com.example.countries.data.model

import com.google.gson.annotations.SerializedName

data class ResponseModel(
    @SerializedName("data")
    val data: List<CountryModel>
)

/*
200 OK
PopulatedPlacesResponse
A list of populated places

400 Bad Request
BadRequestResponse
400 - Bad Request

401 Unauthorized
401 - Unauthorized

403 Forbidden
ForbiddenResponse
403 - Forbidden
 */