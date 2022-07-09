package com.example.weather.view.detailed

import com.example.weather.model.DTO.WeatherDTO

interface OnResponse {
    fun onResponse(weatherDto: WeatherDTO)
    fun onFailResponse(message: String)
}
