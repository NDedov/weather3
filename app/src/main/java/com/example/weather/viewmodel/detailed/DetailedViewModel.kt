package com.example.weather.viewmodel.detailed

import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.WeatherApp
import com.example.weather.domain.City
import com.example.weather.domain.Weather
import com.example.weather.model.*
import com.example.weather.model.retrofit.RepositoryDetailedRetrofitImpl
import java.io.IOException

class DetailedViewModel(private val liveData: MutableLiveData<DetailedAppState> = MutableLiveData<DetailedAppState>()) :
    ViewModel() {


    private lateinit var repositoryLocationToWeather: RepositoryWeatherByCity
    lateinit var repositoryWeatherAddable: RepositoryWeatherSave

    fun getLiveData(): MutableLiveData<DetailedAppState> {
        choiceRepository()
        return liveData
    }

    private fun choiceRepository() {

        if (isConnection()) {
            repositoryLocationToWeather = when (2) {
                1 -> {
                    WeatherApp.repositoryDetailedOkHttpCommon
                }
                2 -> {
                    WeatherApp.repositoryDetailedRetrofitCommon
                }
                3 -> {
                    WeatherApp.repositoryDetailedLoaderCommon
                }
                4 -> {
                    WeatherApp.repositoryDetailedRoomCommon
                }
                else -> {
                    WeatherApp.repositoryDetailedLocalCommon
                }
            }

            repositoryWeatherAddable = when (0) {
                1 -> {
                    WeatherApp.repositoryDetailedRoomCommon
                }
                else -> {
                    WeatherApp.repositoryDetailedRoomCommon
                }
            }
        } else {
            repositoryLocationToWeather = when (2) {
                1 -> {
                    WeatherApp.repositoryDetailedRoomCommon
                }
                2 -> {
                    WeatherApp.repositoryDetailedLocalCommon
                }
                else -> {
                    WeatherApp.repositoryDetailedLocalCommon
                }
            }
            repositoryWeatherAddable = when (0) {
                1 -> {
                    WeatherApp.repositoryDetailedRoomCommon
                }
                else -> {
                    WeatherApp.repositoryDetailedRoomCommon
                }
            }
        }
    }

    fun getWeather(city: City) {
        liveData.value = DetailedAppState.Loading
        repositoryLocationToWeather.getWeather(city, callback)
    }

    private val callback = object : CommonWeatherCallback {
        override fun onResponse(weather: Weather) {
            if (isConnection()){
                Thread{
                    repositoryWeatherAddable.addWeather(weather)
                }.start()
            }
            liveData.postValue(DetailedAppState.Success(weather))
        }

        override fun onFailure(e: IOException) {
            liveData.postValue(DetailedAppState.Error(e))
        }

        override fun onError(message: String) {
            liveData.postValue(DetailedAppState.Error(RuntimeException(message)))
        }
    }


    private fun isConnection(): Boolean {
        val connectivityManager = WeatherApp.getMyApp().getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}
