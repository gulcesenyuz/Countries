package com.example.countries.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countries.data.model.CountryModel
import com.example.countries.data.model.DetailResponseModel
import com.example.countries.data.model.ResponseModel
import com.example.countries.data.repository.Repository
import com.example.countries.ui.adapter.CountryAdapter
import com.example.countries.util.FirestoreRepository
import com.example.countries.util.NetworkResponse
import com.example.countries.util.OnClick
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    private val firestoreRepository: FirestoreRepository
) : ViewModel() {

    private val _countriesList: MutableLiveData<List<CountryModel>?> = MutableLiveData()
    val countriesList: MutableLiveData<List<CountryModel>?> get() = _countriesList

    private val _countryDetail: MutableLiveData<DetailResponseModel?> = MutableLiveData()
    val countryDetail: MutableLiveData<DetailResponseModel?> get() = _countryDetail

    private val _countriesFav: MutableLiveData<List<CountryModel>?> = MutableLiveData()
    val countriesFav: MutableLiveData<List<CountryModel>?> get() = _countriesFav

    var country: CountryModel = CountryModel()

    fun getCountriesFromApi(context: Context, limit: String) {
        viewModelScope.launch {
            repository.getAllCountries(limit).collect { response ->

                when (response) {
                    is NetworkResponse.Loading -> {

                    }
                    is NetworkResponse.Success -> {
                        response.data?.let {
                            if (countriesList.value == null) {
                                firestoreRepository.saveAllToFireStore(context, it)
                            }
                        }
                        //getAllCountries()

                    }
                    is NetworkResponse.Error -> {

                    }
                }
            }
        }
    }

    fun getAllCountries(): MutableLiveData<List<CountryModel>?> {
        viewModelScope.launch {
            firestoreRepository.getAllCountriesFromCollection()
                .addSnapshotListener(
                    EventListener<QuerySnapshot> { value, error ->

                        if (error != null) {
                            Log.d("getAllCountries:", "Listen failed $error")
                            _countriesList.value = null
                            return@EventListener
                        }
                        var allCountyList: MutableList<CountryModel> = mutableListOf()

                        for (doc in value!!) {
                            var countryItem = doc.toObject(CountryModel::class.java)
                            allCountyList.add(countryItem)
                        }
                        for (doc in value.documentChanges) {
                            when (doc.type) {
                                DocumentChange.Type.MODIFIED -> {
                                    for (x in allCountyList) {
                                        Log.d("getAllCountries:", x.name + " : " + x.isFav)

                                    }

                                }
                            }
                        }
                        _countriesList.value = allCountyList

                    })

        }
        return _countriesList
    }

    fun getCountryDetail(countryCode: String) {
        viewModelScope.launch {
            repository.getCountryDetails(countryCode).collect { response ->

                when (response) {
                    is NetworkResponse.Loading -> {

                    }
                    is NetworkResponse.Success -> {
                        _countryDetail.postValue(response.data)

                    }
                    is NetworkResponse.Error -> {

                    }
                }
            }
        }
    }

    fun getDetail(countryCode: String): CountryModel {
        viewModelScope.launch {
            firestoreRepository.getDetailCountry(countryCode).addSnapshotListener { value, error ->
                if (value != null) {
                    country.isFav = value.data?.get("fav") as Boolean
                    country.code = value.data!!["code"] as String
                    country.name = value.data!!["name"] as String

                }
            }

        }
        return country
    }

    fun saveCountry(context: Context, country: CountryModel) {
        viewModelScope.launch {
            country.changeFavState(true)
            firestoreRepository.saveToFireStore(context, country)
            firestoreRepository.changeFavState(country.code, country.isFav)
        }
    }

    fun deleteCountry(context: Context, country: CountryModel) {
        viewModelScope.launch {
            country.changeFavState(false)
            firestoreRepository.deleteFromFirestore(context, country.code)
            firestoreRepository.changeFavState(country.code, country.isFav)
        }
    }


    fun getFavouriteCountries(): MutableLiveData<List<CountryModel>?> {
        viewModelScope.launch {
            firestoreRepository.getFavouriteCountriesFromCollection().addSnapshotListener(
                EventListener<QuerySnapshot> { value, error ->
                    if (error != null) {
                        Log.d("getFavouriteCountries:", "Listen failed $error")
                        _countriesFav.value = null
                        return@EventListener
                    }
                    var savedCountyList: MutableList<CountryModel> = mutableListOf()

                    for (doc in value!!) {
                        var countryItem = doc.toObject(CountryModel::class.java)
                        savedCountyList.add(countryItem)
                    }
                    _countriesFav.value = savedCountyList
                    Log.d("getFavouriteCountries:", savedCountyList.toString())

                })
        }
        return _countriesFav
    }

}



