package com.example.weather.model

import com.example.weather.domain.Weather

class RepositoryRemoteImpl:RepositoryMulti, RepositorySingle {

    override fun getWeather(lat: Double, lon: Double): Weather {
        TODO("Not yet implemented")
    }

    override fun getListWeather(location: Location): List<Weather> {
        TODO("Not yet implemented")
    }
}