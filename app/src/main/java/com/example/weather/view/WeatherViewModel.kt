package com.example.weather.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.domain.Weather
import com.example.weather.viewmodel.AppState

class WeatherViewModel(val liveData: MutableLiveData<AppState> = MutableLiveData<AppState>()) :
    ViewModel() {

    fun sendRequest() {
        liveData.value = AppState.Loading
        Thread{
            Thread.sleep(2000L)
            liveData.postValue(AppState.Success(Weather()))
        }.start()
    }
}