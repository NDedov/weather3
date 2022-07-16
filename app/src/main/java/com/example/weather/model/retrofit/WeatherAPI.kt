package com.example.weather.model.retrofit

import com.example.weather.model.DTO.WeatherDTO
import com.example.weather.utils.YANDEX_API_KEY
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface WeatherAPI {

    @GET("/v2/informers")
    fun getWeatherYourSite(
        @Header(YANDEX_API_KEY) keyValue: String,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ):Call<WeatherDTO>

    @GET("/v2/forecast")
    fun getWeatherTestRate(
        @Header(YANDEX_API_KEY) keyValue: String,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ):Call<WeatherDTO>

}