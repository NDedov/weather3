package com.example.weather.viewmodel.detailed

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.model.*
import com.example.weather.model.DTO.WeatherDTO
import com.example.weather.model.retrofit.RepositoryDetailedRetrofitImpl
import java.io.IOException

class DetailedViewModel(private val liveData: MutableLiveData<DetailedAppState> = MutableLiveData<DetailedAppState>()) :
    ViewModel() {

    private lateinit var repository: RepositoryDetailed


    fun getWeather(lat: Double, lon: Double) {
        choiceRepository()
        liveData.value = DetailedAppState.Loading
        repository.getWeather(lat, lon, callback)
    }

    private val callback = object : RepositoryCommonCallback {
        override fun onResponse(weatherDTO: WeatherDTO) {
            liveData.postValue(DetailedAppState.Success(weatherDTO))
        }

        override fun onFailure(e: IOException) {
            liveData.postValue(DetailedAppState.Error(e))
        }

        override fun onError(message: String) {
            liveData.postValue(DetailedAppState.Error(RuntimeException(message)))
        }

    }

    fun getLiveData(): MutableLiveData<DetailedAppState> {
        choiceRepository()
        return liveData
    }

    private fun choiceRepository() {
        repository = if (!isConnection()) {
            RepositoryDetailedLocalImpl()
        } else {
            when (5) {
                1 -> {
                    RepositoryDetailedOkHttpImpl()
                }
                2 -> {
                    RepositoryDetailedRetrofitImpl()
                }
                else -> {
                    RepositoryDetailedWeatherLoaderImpl()
                }
            }
        }
    }

    private fun isConnection(): Boolean {
        //TODO сделать проверку
        return false
    }
}
