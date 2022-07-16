package com.example.weather.viewmodel.detailed

import com.example.weather.model.DTO.WeatherDTO

sealed class DetailedAppState {
    data class Success(val weather: WeatherDTO) : DetailedAppState()
    data class Error(val error: Throwable) : DetailedAppState()
    object Loading: DetailedAppState()
}