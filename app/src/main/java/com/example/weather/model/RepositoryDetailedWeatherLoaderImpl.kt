package com.example.weather.model

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.weather.domain.City
import com.example.weather.model.DTO.WeatherDTO
import com.example.weather.utils.BuildConfig_WEATHER_API_KEY
import com.example.weather.utils.YANDEX_API_KEY
import com.example.weather.utils.bindDTOWithCity
import com.example.weather.utils.getLines
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class RepositoryDetailedWeatherLoaderImpl : RepositoryWeatherByCity {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun getWeather(city: City, callback: CommonWeatherCallback) {
        val uri = URL("https://api.weather.yandex.ru/v2/informers?lat=${city.lat}&lon=${city.lon}")

        Thread {
            val myConnection: HttpsURLConnection?
            myConnection = uri.openConnection() as HttpsURLConnection
            myConnection.readTimeout = 5000
            myConnection.addRequestProperty(
                YANDEX_API_KEY,
                BuildConfig_WEATHER_API_KEY
            )

            try {
                val reader = BufferedReader(InputStreamReader(myConnection.inputStream))
                val weatherDTO = Gson().fromJson(getLines(reader), WeatherDTO::class.java)
                callback.onResponse(bindDTOWithCity(weatherDTO, city))
            } catch (e: IOException) {
                Log.d("@@@", e.toString())
                callback.onFailure(e)
            } finally {
                myConnection.disconnect()
            }
        }.start()
    }
}
