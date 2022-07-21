package com.example.weather.model

import com.example.weather.domain.City
import com.example.weather.domain.Weather
import java.io.IOException


fun interface RepositoryMulti {
    fun getListWeather(location: Location):List<Weather>
}

interface RepositoryWeatherAvailable {
    fun getWeatherAll(callback: CommonListWeatherCallback)
    fun deleteHistoryByWeather(weather: Weather, callback: CommonListWeatherCallback)

}

interface CommonWeatherCallback{
    fun onResponse(weather: Weather)
    fun onFailure(e: IOException)
    fun onError (message: String)
}

interface CommonListWeatherCallback{
    fun onResponse(weatherList: List<Weather>)
    fun onFailure(e: IOException)
    fun onDelete(num: Int)
}

fun interface RepositoryWeatherByCity {
    fun getWeather(city: City, callback: CommonWeatherCallback)
}

fun interface RepositoryWeatherSave {
    fun addWeather(weather: Weather)
}

sealed class Location{
    object Russian:Location()
    object World:Location()
    object USA: Location()
}


