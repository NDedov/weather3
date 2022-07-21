package com.example.weather.viewmodel.weatherhistorylist

import com.example.weather.domain.Weather

sealed class WeatherHistoryListFragmentAppState {
    data class SuccessMulti(val weatherList: List<Weather>) : WeatherHistoryListFragmentAppState()
    data class Error(val error: Throwable) : WeatherHistoryListFragmentAppState()
    data class SuccessDelete(val num: Int) : WeatherHistoryListFragmentAppState()
    object Loading : WeatherHistoryListFragmentAppState()
}
