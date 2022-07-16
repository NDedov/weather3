package com.example.weather.model

import com.example.weather.domain.Weather
import com.example.weather.model.DTO.WeatherDTO
import java.io.IOException


fun interface RepositoryMulti {
    fun getListWeather(location: Location):List<Weather>
}

fun interface RepositoryDetailed {
    fun getWeather(lat: Double, lon: Double, callback: RepositoryCommonCallback)
}

interface RepositoryCommonCallback{
    fun onResponse(weatherDTO: WeatherDTO)
    fun onFailure(e: IOException)
    fun onError (message: String)
}

sealed class Location{
    object Russian:Location()
    object World:Location()
    object USA: Location()
}