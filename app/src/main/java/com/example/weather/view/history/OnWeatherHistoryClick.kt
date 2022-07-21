package com.example.weather.view.history

import com.example.weather.domain.Weather

interface OnWeatherHistoryClick {
    fun onDeleteClick(weather: Weather)
    fun onItemClick(weather: Weather)
}