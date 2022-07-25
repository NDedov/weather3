package com.example.weather.model

import com.example.weather.BuildConfig
import com.example.weather.domain.City
import com.example.weather.model.DTO.WeatherDTO
import com.example.weather.utils.YANDEX_API_KEY
import com.example.weather.utils.bindDTOWithCity
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class RepositoryDetailedOkHttpImpl : RepositoryWeatherByCity {
    override fun getWeather(city: City, callback: CommonWeatherCallback) {
        val client = OkHttpClient()
        val builder = Request.Builder()
        builder.addHeader(YANDEX_API_KEY, BuildConfig.WEATHER_API_KEY)
        builder.url("https://api.weather.yandex.ru/v2/informers?lat=${city.lat}&lon=${city.lon}")
        val request: Request = builder.build()
        val call: Call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure(e)
            }
            override fun onResponse(call: Call, response: Response) {
                if (response.code in 200..299 && response.body != null) {
                    response.body?.let {
                        val responseString = it.string()
                        val weatherDTO =
                            Gson().fromJson((responseString), WeatherDTO::class.java)
                        callback.onResponse(bindDTOWithCity(weatherDTO,city))
                    }
                } else {
                    callback.onError(response.code.toString())
                }
            }
        })
    }
}