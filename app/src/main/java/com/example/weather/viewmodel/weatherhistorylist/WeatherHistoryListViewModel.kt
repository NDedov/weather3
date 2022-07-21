package com.example.weather.viewmodel.weatherhistorylist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.domain.Weather
import com.example.weather.model.CommonListWeatherCallback
import com.example.weather.model.RepositoryRoomImpl
import com.example.weather.model.RepositoryWeatherAvailable
import java.io.IOException

class WeatherHistoryListViewModel(private val liveData: MutableLiveData<WeatherHistoryListFragmentAppState> = MutableLiveData<WeatherHistoryListFragmentAppState>()) :
    ViewModel() {

    lateinit var repository: RepositoryWeatherAvailable

    fun getLiveData(): MutableLiveData<WeatherHistoryListFragmentAppState> {
        choiceRepository()
        return liveData
    }

    private fun choiceRepository() {
        repository = RepositoryRoomImpl()
    }

    fun getAllHistory() {
        liveData.value = WeatherHistoryListFragmentAppState.Loading
        Thread {
            repository.getWeatherAll(callback)
        }.start()
    }

    fun deleteHistoryByCity(weather: Weather) {
        Thread {
            repository.deleteHistoryByWeather(weather, callback)
        }.start()
    }

    private val callback = object : CommonListWeatherCallback {
        override fun onResponse(weatherList: List<Weather>) {
            liveData.postValue(WeatherHistoryListFragmentAppState.SuccessMulti(weatherList))
        }

        override fun onFailure(e: IOException) {
            liveData.postValue(WeatherHistoryListFragmentAppState.Error(e))
        }

        override fun onDelete(num: Int) {
            liveData.postValue(WeatherHistoryListFragmentAppState.SuccessDelete(num))
        }
    }

}