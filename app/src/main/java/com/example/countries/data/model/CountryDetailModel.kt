package com.example.countries.data.model

import com.google.gson.annotations.SerializedName

data class CountryDetailModel(

    @SerializedName("code")
    val code: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("flagImageUri")
    val flagImageUri: String? = null,

    @SerializedName("wikiDataId")
    val wikiDataId: String? = null,
)
