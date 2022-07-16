package com.example.weather.viewmodel.citylist

import com.example.weather.domain.Weather

sealed class CityListAppState {
    data class SuccessMulti(val weatherList: List<Weather>) : CityListAppState()
    data class Error(val error: Throwable) : CityListAppState()
    data class Loading (val loadingOver: Boolean) : CityListAppState()
}