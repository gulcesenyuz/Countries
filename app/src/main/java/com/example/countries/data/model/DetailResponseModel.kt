package com.example.countries.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class DetailResponseModel(
    @SerializedName("data")
    val data: CountryDetailModel
)

