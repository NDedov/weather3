package com.example.weather.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.model.Repository
import com.example.weather.model.RepositoryLocalImpl
import com.example.weather.model.RepositoryRemoteImpl
import com.example.weather.viewmodel.AppState

class WeatherViewModel(private val liveData: MutableLiveData<AppState> = MutableLiveData<AppState>()) :
    ViewModel() {

    private lateinit var repository: Repository

    fun sendRequest() {

        liveData.value = AppState.Loading(loadingOver = false)

        val threadLoad = Thread{
            Thread.sleep(2000)
            liveData.postValue(AppState.Loading(loadingOver = true))
        }
        threadLoad.start()

        if((0..2).random() == 1){
            val ex = IllegalStateException("Что-то пошло не так")
            liveData.postValue(AppState.Error(ex))
            throw ex
        }else{
            liveData.postValue(AppState.Success(repository.getWeather(55.755826, 37.617299900000035)))
        }

    }

    fun getLiveData():MutableLiveData<AppState>{
        choiceRepository()
        return liveData
    }

    private fun choiceRepository(){
        repository = if (isConnection()) {
            RepositoryRemoteImpl()
        }else{
            RepositoryLocalImpl()
        }
    }

    private fun isConnection(): Boolean {
        if ((0..3).random() == 1)
            return false
        return true
    }

}
