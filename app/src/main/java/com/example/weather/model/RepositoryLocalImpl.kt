package com.example.weather.model

import com.example.weather.domain.Weather
import com.example.weather.domain.getRussianCities
import com.example.weather.domain.getUSACities
import com.example.weather.domain.getWorldCities

class RepositoryLocalImpl:RepositoryMulti, RepositorySingle {
    override fun getWeather(lat: Double, lon: Double): Weather {
        return Weather()
    }

    override fun getListWeather(location: Location): List<Weather> {
        return when (location){
            Location.Russian ->{
                getRussianCities()
            }
            Location.World -> {
                getWorldCities()
            }
            Location.USA -> {
                getUSACities()
            }
        }
    }
}