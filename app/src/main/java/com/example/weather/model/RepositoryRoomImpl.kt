package com.example.weather.model

import com.example.weather.WeatherApp
import com.example.weather.domain.City
import com.example.weather.domain.Weather
import com.example.weather.model.room.WeatherEntity


class RepositoryRoomImpl:RepositoryWeatherByCity,RepositoryWeatherSave,RepositoryWeatherAvailable {
    override fun getWeather(city: City, callback: CommonWeatherCallback) {
        callback.onResponse(WeatherApp.getWeatherDatabase().weatherDao().getWeatherByLocation(city.lat,city.lon).let{
            convertHistoryEntityToWeather(it).last()
        })
    }

    override fun addWeather(weather: Weather) {
        WeatherApp.getWeatherDatabase().weatherDao().insertRoom(convertWeatherToEntity(weather))
    }

    override fun getWeatherAll(callback: CommonListWeatherCallback) {
        callback.onResponse(convertHistoryEntityToWeather(WeatherApp.getWeatherDatabase().weatherDao().getWeatherAll()))
    }

    private fun convertHistoryEntityToWeather(entityList: List<WeatherEntity>): List<Weather> {
        return entityList.map {
            Weather(City(it.name, it.lat, it.lon), it.temperature, it.feelsLike, date = it.dateTime)
        }
    }
    private fun convertWeatherToEntity(weather: Weather): WeatherEntity {
        return WeatherEntity(0, weather.city.name, weather.city.lat,weather.city.lon, weather.temp, weather.feelsLike,System.currentTimeMillis())
    }

    override fun deleteHistoryByWeather(weather: Weather, callback: CommonListWeatherCallback) {
        callback.onDelete(WeatherApp.getWeatherDatabase().weatherDao().deleteByTime(weather.date))
    }

}

