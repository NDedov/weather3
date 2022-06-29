package com.example.weather.view.detailed

import com.example.weather.domain.Weather

fun interface OnWeatherListItemClick {
    fun onItemClick(weather: Weather)
}