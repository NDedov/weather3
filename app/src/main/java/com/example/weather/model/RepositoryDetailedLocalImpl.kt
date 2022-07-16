package com.example.weather.model

import com.example.weather.domain.*
import com.example.weather.model.DTO.FactDTO
import com.example.weather.model.DTO.WeatherDTO

class RepositoryDetailedLocalImpl : RepositoryDetailed {
    override fun getWeather(lat: Double, lon: Double, callback: RepositoryCommonCallback) {
        val list = getWorldCities().toMutableList()
        list.addAll(getRussianCities())
        list.addAll(getUSACities())
        val res = list.find { it.city.lat == lat && it.city.lon == lon }
        /*val response = list.filter { it.city.lat == lat && it.city.lon == lon }
        callback.onResponse(convertModelToDto(response.first()))*/
        if (res != null)
            callback.onResponse(convertModelToDto(res))
        else
            callback.onError("Ошибка поиска, город не найден!")

    }

    private fun convertDtoToModel(weatherDTO: WeatherDTO): Weather {
        val fact: FactDTO = weatherDTO.fact
        return (Weather(getDefaultCity(), fact.temp, fact.feelsLike))
    }

    private fun convertModelToDto(weather: Weather): WeatherDTO {
        val fact: FactDTO = FactDTO(weather.condition, weather.feelsLike, "", weather.temp)
        return WeatherDTO(fact)
    }
}
