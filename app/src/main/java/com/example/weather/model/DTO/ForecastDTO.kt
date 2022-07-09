package com.example.weather.model.DTO


import com.google.gson.annotations.SerializedName

data class ForecastDTO(
    @SerializedName("biomet")
    val biomet: BiometDTO,
    @SerializedName("date")
    val date: String,
    @SerializedName("date_ts")
    val dateTs: Int,
    @SerializedName("hours")
    val hours: List<HourDTO>,
    @SerializedName("moon_code")
    val moonCode: Int,
    @SerializedName("moon_text")
    val moonText: String,
    @SerializedName("parts")
    val parts: PartsDTO,
    @SerializedName("rise_begin")
    val riseBegin: String,
    @SerializedName("set_end")
    val setEnd: String,
    @SerializedName("sunrise")
    val sunrise: String,
    @SerializedName("sunset")
    val sunset: String,
    @SerializedName("week")
    val week: Int
)