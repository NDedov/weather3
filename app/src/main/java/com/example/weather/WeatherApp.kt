package com.example.weather

import android.app.Application
import androidx.room.Room
import com.example.weather.model.RepositoryDetailedLocalImpl
import com.example.weather.model.RepositoryDetailedOkHttpImpl
import com.example.weather.model.RepositoryDetailedWeatherLoaderImpl
import com.example.weather.model.RepositoryRoomImpl
import com.example.weather.model.retrofit.RepositoryDetailedRetrofitImpl
import com.example.weather.model.room.WeatherDatabase
import com.example.weather.utils.ROOM_DB_WEATHER

class WeatherApp : Application() {
    override fun onCreate() {
        super.onCreate()
        myApp = this
    }

    companion object {
        val repositoryDetailedRetrofitCommon = RepositoryDetailedRetrofitImpl()
        val repositoryDetailedRoomCommon = RepositoryRoomImpl()
        val repositoryDetailedOkHttpCommon =  RepositoryDetailedOkHttpImpl()
        val repositoryDetailedLocalCommon = RepositoryDetailedLocalImpl()
        val repositoryDetailedLoaderCommon = RepositoryDetailedWeatherLoaderImpl()

        private var myApp: WeatherApp? = null
        private var weatherDatabase: WeatherDatabase? = null
        fun getMyApp() = myApp!!
        fun getWeatherDatabase(): WeatherDatabase {
            if (weatherDatabase == null) {
                weatherDatabase = Room.databaseBuilder(
                    getMyApp(),
                    WeatherDatabase::class.java,
                    ROOM_DB_WEATHER
                )
                    .build()
            }
            return weatherDatabase!!
        }
    }
}