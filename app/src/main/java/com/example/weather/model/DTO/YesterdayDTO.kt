package com.example.weather.model.DTO


import com.google.gson.annotations.SerializedName

data class YesterdayDTO(
    @SerializedName("temp")
    val temp: Int
)