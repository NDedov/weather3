package com.example.weather.utils

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.weather.model.DTO.WeatherDTO
import com.example.weather.view.detailed.OnResponse
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.net.UnknownHostException
import javax.net.ssl.HttpsURLConnection

object WeatherLoader {

    @RequiresApi(Build.VERSION_CODES.N)
    fun request(lat: Double, lon: Double, onResponse: OnResponse) {
        val uri = URL("https://api.weather.yandex.ru/v2/forecast?lat=$lat&lon=$lon")
        val myConnection: HttpsURLConnection?

        myConnection = uri.openConnection() as HttpsURLConnection
        myConnection.readTimeout = 5000
        myConnection.addRequestProperty("X-Yandex-API-Key", "9d8f52b7-e22d-45fe-b38a-505e9c40b6f3")
        Thread {
            try {
                val reader = BufferedReader(InputStreamReader(myConnection.inputStream))
                val weatherDTO = Gson().fromJson(getLines(reader), WeatherDTO::class.java)
                onResponse.onResponse(weatherDTO)
            } catch (e: UnknownHostException) {
                Log.d("@@@", e.toString())
                onResponse.onFailResponse(e.toString())
            }
        }.start()

    }
}