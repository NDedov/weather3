package com.example.weather.model.DTO


import com.google.gson.annotations.SerializedName

data class ProvinceDTO(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)