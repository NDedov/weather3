package com.example.weather.model

import com.example.weather.domain.Weather

interface Repository {
    fun getListWeather():List<Weather>
    fun getWeather( lat: Double, lon: Double):Weather
}