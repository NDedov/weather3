package com.example.weather.model.DTO


import com.google.gson.annotations.SerializedName

data class BiometDTO(
    @SerializedName("condition")
    val condition: String,
    @SerializedName("index")
    val index: Int
)