package com.example.weather.model.DTO


import com.google.gson.annotations.SerializedName

data class TzinfoDTO(
    @SerializedName("abbr")
    val abbr: String,
    @SerializedName("dst")
    val dst: Boolean,
    @SerializedName("name")
    val name: String,
    @SerializedName("offset")
    val offset: Int
)