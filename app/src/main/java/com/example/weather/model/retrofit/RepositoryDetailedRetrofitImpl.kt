package com.example.weather.model.retrofit

import com.example.weather.BuildConfig
import com.example.weather.model.DTO.WeatherDTO
import com.example.weather.model.RepositoryCommonCallback
import com.example.weather.model.RepositoryDetailed
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class RepositoryDetailedRetrofitImpl : RepositoryDetailed {
    override fun getWeather(lat: Double, lon: Double, callback: RepositoryCommonCallback) {
        val retrofitImpl = Retrofit.Builder()
        retrofitImpl.baseUrl("https://api.weather.yandex.ru")
        retrofitImpl.addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        val api = retrofitImpl.build().create(WeatherAPI::class.java)
        //api.getWeather(BuildConfig.WEATHER_API_KEY,lat,lon).execute() // синхронный запрос
        api.getWeatherTestRate(BuildConfig.WEATHER_API_KEY, lat, lon)
            .enqueue(object : Callback<WeatherDTO> {
                override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
                    // response.raw().request // тут есть информация - а кто же нас вызвал
                    if (response.isSuccessful && response.body() != null) {
                        callback.onResponse(response.body()!!)
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