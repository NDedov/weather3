package com.example.weather.viewmodel.detailed

import com.example.weather.domain.Weather

sealed class DetailedAppState {
    data class Success(val weather: Weather) : DetailedAppState()
    data class Error(val error: Throwable) : DetailedAppState()
    object Loading: DetailedAppState()
}