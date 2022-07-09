package com.example.weather.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.io.BufferedReader
import java.util.stream.Collectors

class Utils {
}

@RequiresApi(Build.VERSION_CODES.N)
fun getLines(reader: BufferedReader): String {
    return reader.lines().collect(Collectors.joining("\n"))
}

fun conditionTranslate(stringInput: String): String {
    return when (stringInput) {
        "clear" -> "ясно"
        "partly-cloudy" -> "малооблачно"
        "cloudy" -> "облачно с прояснениями"
        "overcast" -> "пасмурно"
        "drizzle" -> "морось"
        "light-rain" -> "небольшой дождь"
        "rain" -> "дождь"
        "moderate-rain" -> "умеренно сильный дождь"
        "heavy-rain" -> "сильный дождь"
        "continuous-heavy-rain" -> "длительный сильный дождь"
        "showers" -> "ливень"
        "wet-snow" -> "дождь со снегом"
        "light-snow" -> "небольшой снег"
        "snow" -> "снег"
        "snow-showers" -> "снегопад"
        "hail" -> "град"
        "thunderstorm" -> "гроза"
        "thunderstorm-with-rain" -> "дождь с грозой"
        "thunderstorm-with-hail" -> "гроза с градом"
        else -> stringInput
    }
}

