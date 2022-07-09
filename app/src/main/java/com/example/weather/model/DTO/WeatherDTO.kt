package com.example.weather.model.DTO


import com.google.gson.annotations.SerializedName

data class WeatherDTO(
    @SerializedName("fact")
    val fact: FactDTO,
    @SerializedName("forecasts")
    val forecasts: List<ForecastDTO>,
    @SerializedName("geo_object")
    val geoObject: GeoObjectDTO,
    @SerializedName("info")
    val info: InfoDTO,
    @SerializedName("now")
    val now: Int,
    @SerializedName("now_dt")
    val nowDt: String,
    @SerializedName("yesterday")
    val yesterday: YesterdayDTO
)