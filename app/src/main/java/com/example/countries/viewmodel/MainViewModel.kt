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
import com.example.countries.util.FirestoreRepository
import com.example.countries.util.NetworkResponse
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    private val firestoreRepository: FirestoreRepository
) : ViewModel() {

    private val _countriesList: MutableLiveData<ResponseModel?> = MutableLiveData()
    val countriesList: MutableLiveData<ResponseModel?> get() = _countriesList

    private val _countryDetail: MutableLiveData<DetailResponseModel?> = MutableLiveData()
    val countryDetail: MutableLiveData<DetailResponseModel?> get() = _countryDetail

    private val _countriesFav: MutableLiveData<List<CountryModel>?> = MutableLiveData()
    val countriesFav: MutableLiveData<List<CountryModel>?> get() = _countriesFav

    fun getCountries(limit: String) {
        viewModelScope.launch {
            repository.getAllCountries(limit).collect { response ->

                when (response) {
                    is NetworkResponse.Loading -> {

                    }
                    is NetworkResponse.Success -> {
                        _countriesList.postValue(response.data)
                        Log.d("VM REPosnse:", response.data.toString())
                    }
                    is NetworkResponse.Error -> {

                    }
                }
            }
        }
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

    fun saveCountry(context: Context, country: CountryModel, code: String) {
        viewModelScope.launch {
            firestoreRepository.saveToFireStore(context, country, code)
        }
    }

    fun deleteCountry(context: Context, code: String) {
        viewModelScope.launch {
            firestoreRepository.deleteFromFirestore(context, code)
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
                    _countriesFav.value= savedCountyList
                    Log.d("getFavouriteCountries:", savedCountyList.toString())

                })

        }
        return _countriesFav
    }

}



