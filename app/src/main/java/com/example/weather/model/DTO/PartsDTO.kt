package com.example.weather.model.DTO


import com.google.gson.annotations.SerializedName

data class PartsDTO(
    @SerializedName("day")
    val day: DayDTO,
    @SerializedName("day_short")
    val dayShort: DayShortDTO,
    @SerializedName("evening")
    val evening: EveningDTO,
    @SerializedName("morning")
    val morning: MorningDTO,
    @SerializedName("night")
    val night: NightDTO,
    @SerializedName("night_short")
    val nightShort: NightShortDTO
)