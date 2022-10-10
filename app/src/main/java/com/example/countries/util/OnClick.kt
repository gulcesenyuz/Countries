package com.example.countries.util

import com.example.countries.data.model.CountryModel

interface OnClick {
    fun onClickCountry(country: CountryModel)
}