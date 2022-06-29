package com.example.weather.view.weatherlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.model.*
import com.example.weather.viewmodel.AppState

class WeatherViewModel(private val liveData: MutableLiveData<AppState> = MutableLiveData<AppState>()) :
    ViewModel() {

    private lateinit var repositoryMulti: RepositoryMulti
    private lateinit var repositorySingle: RepositorySingle

    fun sendRequest(location:Location) {

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
            liveData.postValue(AppState.SuccessMulti(repositoryMulti.getListWeather(location)))
        }
    }

    fun getLiveData():MutableLiveData<AppState>{
        choiceRepository()
        return liveData
    }

    private fun choiceRepository(){
        repositorySingle = if (isConnection()) {
            RepositoryRemoteImpl()
        }else{
            RepositoryLocalImpl()
        }
        repositoryMulti = RepositoryLocalImpl()
    }

    private fun isConnection(): Boolean {
        if ((0..3).random() == 1)
            return false
        return true
    }
}
