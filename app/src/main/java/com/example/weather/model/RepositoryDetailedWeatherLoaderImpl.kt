package com.example.weather.model

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.weather.BuildConfig
import com.example.weather.model.DTO.WeatherDTO
import com.example.weather.utils.YANDEX_API_KEY
import com.example.weather.utils.getLines
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class RepositoryDetailedWeatherLoaderImpl : RepositoryDetailed {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun getWeather(lat: Double, lon: Double, callback: RepositoryCommonCallback) {
        val uri = URL("https://api.weather.yandex.ru/v2/forecast?lat=$lat&lon=$lon")

        Thread {
            val myConnection: HttpsURLConnection?
            myConnection = uri.openConnection() as HttpsURLConnection
            myConnection.readTimeout = 5000
            myConnection.addRequestProperty(
                YANDEX_API_KEY,
                BuildConfig.WEATHER_API_KEY
            )

            try {
                val reader = BufferedReader(InputStreamReader(myConnection.inputStream))
                val weatherDTO = Gson().fromJson(getLines(reader), WeatherDTO::class.java)
                callback.onResponse(weatherDTO)
            } catch (e: IOException) {
                Log.d("@@@", e.toString())
                callback.onFailure(e)
            } finally {
                myConnection.disconnect()
            }
        }.start()
    }
}
