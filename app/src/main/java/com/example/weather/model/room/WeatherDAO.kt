package com.example.weather.model.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WeatherDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE) // FIXME создать конфликт
    fun insertRoom(weatherEntity:WeatherEntity)

    @Query("INSERT INTO weather_entity_table (name,lat,lon,temperature,feelsLike) VALUES(:name,:lat,:lon,:temperature,:feelsLike)")
    fun insertNative1(name:String,lat:Double,lon:Double,temperature:Int,feelsLike:Int)

    @Query("INSERT INTO weather_entity_table (id,name,lat,lon,temperature,feelsLike) VALUES(:id,:name,:lat,:lon,:temperature,:feelsLike)")
    fun insertNative2(id:Long,name:String,lat:Double,lon:Double,temperature:Int,feelsLike:Int)

    @Query("SELECT * FROM weather_entity_table WHERE lat=:mLat AND lon=:mLon")
    fun getWeatherByLocation(mLat:Double,mLon:Double):List<WeatherEntity>

    @Query("SELECT * FROM weather_entity_table")
    fun getWeatherAll():List<WeatherEntity>

    @Query("DELETE FROM weather_entity_table WHERE dateTime=:mDateTime")
    fun deleteByTime(mDateTime:Long): Int

    // CRUD
    // INSERT INTO table_name (key1,key2) VALUES(value1,value2)
    // SELECT * FROM table_name WHERE key1=1
    // SELECT * FROM table_name WHERE lat IN(30,31,32,40)
    // SELECT * FROM table_name WHERE lat  between 1 and 10
    // UPDATE table_name SET name=newName WHERE key1=1
    // DELETE FROM table_name WHERE key1=1

}