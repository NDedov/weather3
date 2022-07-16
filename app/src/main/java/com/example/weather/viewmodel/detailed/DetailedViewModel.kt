package com.example.weather.viewmodel.detailed

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.model.*
import com.example.weather.model.DTO.WeatherDTO
import java.io.IOException

class DetailedViewModel(private val liveData: MutableLiveData<DetailedAppState> = MutableLiveData<DetailedAppState>()) :
    ViewModel() {

    private lateinit var repository: RepositoryDetailed


    fun getWeather(lat: Double, lon: Double) {
        choiceRepository()
        liveData.value = DetailedAppState.Loading
        repository.getWeather(lat, lon, callback)
//        liveData.postValue(DetailedAppState.Success(repository.getWeather(lat, lon)))
    }

    private val callback = object : RepositoryCommonCallback {
        override fun onResponse(weatherDTO: WeatherDTO) {
            TODO("Not yet implemented")
        }

        override fun onFailure(e: IOException) {
            TODO("Not yet implemented")
        }

        override fun onError(message: String) {
            TODO("Not yet implemented")
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
            when (1) {
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
        return true
    }
}
