package com.example.countries.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countries.data.model.ResponseModel
import com.example.countries.data.repository.Repository
import com.example.countries.util.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.security.auth.login.LoginException

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _countriesList: MutableLiveData<ResponseModel?> = MutableLiveData()
    val countriesList : MutableLiveData<ResponseModel?> get() = _countriesList


      fun getCountries(limit:String){
      viewModelScope.launch {
          repository.getAllCountries(limit).collect{response->

              when(response){
                  is NetworkResponse.Loading->{

                  }
                  is NetworkResponse.Success->{
                      _countriesList.postValue(response.data)
                      Log.d("VM REPosnse:", response.data.toString())
                  }
                  is NetworkResponse.Error->{

                  }
              }
              }
          }
      }

}