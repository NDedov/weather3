package com.example.weather.viewmodel

import com.example.weather.domain.Weather

sealed class AppState {
    data class SuccessSingle(val weatherData: Weather) : AppState()
    data class SuccessMulti(val weatherList: List<Weather>) : AppState()
    data class Error(val error: Throwable) : AppState()
    data class Loading (val loadingOver: Boolean) : AppState()
}