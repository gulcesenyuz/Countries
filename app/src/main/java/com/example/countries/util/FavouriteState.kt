package com.example.countries.util

import com.example.countries.data.model.CountryModel

interface FavouriteState {
     fun checkFavState(country: CountryModel, isFav:Boolean)
}