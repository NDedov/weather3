package com.example.weather.model.retrofit

import com.example.weather.BuildConfig
import com.example.weather.domain.City
import com.example.weather.model.CommonWeatherCallback
import com.example.weather.model.DTO.WeatherDTO
import com.example.weather.model.RepositoryWeatherByCity
import com.example.weather.utils.bindDTOWithCity

import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class RepositoryDetailedRetrofitImpl : RepositoryWeatherByCity {
    override fun getWeather(city: City, callback: CommonWeatherCallback) {
        val retrofitImpl = Retrofit.Builder()
        retrofitImpl.baseUrl("https://api.weather.yandex.ru")
        retrofitImpl.addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        val api = retrofitImpl.build().create(WeatherAPI::class.java)
        //api.getWeather(BuildConfig.WEATHER_API_KEY,lat,lon).execute() // синхронный запрос
        api.getWeatherTestRate(BuildConfig.WEATHER_API_KEY, city.lat, city.lon)
            .enqueue(object : Callback<WeatherDTO> {
                override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
                    // response.raw().request // тут есть информация - а кто же нас вызвал
                    if (response.isSuccessful && response.body() != null) {
                        callback.onResponse(bindDTOWithCity(response.body()!!,city))
                    } else {
                        callback.onError(response.message().toString())
                    }
                }
                override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
                    callback.onFailure(t as IOException) //костыль
                }
            })

    }
}