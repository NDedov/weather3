package com.example.weather.model

import com.example.weather.domain.City
import com.example.weather.domain.getRussianCities
import com.example.weather.domain.getUSACities
import com.example.weather.domain.getWorldCities

class RepositoryDetailedLocalImpl : RepositoryWeatherByCity {
    override fun getWeather(city: City, callback: CommonWeatherCallback) {
        val list = getWorldCities().toMutableList()
        list.addAll(getRussianCities())
        list.addAll(getUSACities())
        val res = list.find { it.city.lat == city.lat && it.city.lon == city.lon }

        if (res != null)
            callback.onResponse(res)
        else
            callback.onError("Ошибка поиска, город не найден!")

    }

}
