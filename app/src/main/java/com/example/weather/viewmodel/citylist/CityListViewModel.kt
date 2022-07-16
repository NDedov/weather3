package com.example.weather.viewmodel.citylist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.model.Location
import com.example.weather.model.RepositoryLocalImpl
import com.example.weather.model.RepositoryMulti

class CityListViewModel(private val liveData: MutableLiveData<CityListAppState> = MutableLiveData<CityListAppState>()) :
    ViewModel() {

    private lateinit var repositoryMulti: RepositoryMulti

    fun sendRequest(location:Location) {

        liveData.value = CityListAppState.Loading(loadingOver = false)

        Thread{
            Thread.sleep(500)
            liveData.postValue(CityListAppState.Loading(loadingOver = true))
        }.start()

        if (false){
            val ex = IllegalStateException("Что-то пошло не так")
            liveData.postValue(CityListAppState.Error(ex))
            throw ex
        }else{
            liveData.postValue(CityListAppState.SuccessMulti(repositoryMulti.getListWeather(location)))
        }
    }

    fun getLiveData():MutableLiveData<CityListAppState>{
        choiceRepository()
        return liveData
    }

    private fun choiceRepository(){
        repositoryMulti = RepositoryLocalImpl()
    }

    private fun isConnection(): Boolean {
        if ((0..3).random() == 1)
            return false
        return true
    }
}
