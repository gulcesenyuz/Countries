package com.example.countries.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/*
 {
      "id": 2987553,
      "wikiDataId": "Q24371",
      "type": "CITY",
      "name": "L'Aldosa de Canillo",
      "country": "Andorra",
      "countryCode": "AD",
      "region": "Canillo",
      "regionCode": 2,
      "latitude": 42.57777778,
      "longitude": 1.61944444
    }
 */

@Parcelize
data class CountryModel(

    @SerializedName("name")
    val name: String = "empty name",
    @SerializedName("code")
    val code: String = "empty code"

):Parcelable