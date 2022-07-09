package com.example.weather.model.DTO


import com.google.gson.annotations.SerializedName

data class GeoObjectDTO(
    @SerializedName("country")
    val country: CountryDTO,
    @SerializedName("district")
    val district: Any,
    @SerializedName("locality")
    val locality: Any,
    @SerializedName("province")
    val province: ProvinceDTO
)