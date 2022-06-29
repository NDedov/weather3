package com.example.weather.domain

data class Weather(
    val city: City = getDefaultCity(),
    val temp: Int = 20,
    val feelsLike: Int = 20,
    val condition: String = "Ясно",
    val windSpeed: Int = 10,
    val windGust: Int = 5,
    val windDir: String = "северное",
    val pressure_mm: Int = 854,
    val humidity: Int = 52,
)

data class City(
    val name: String,
    val lat: Double,
    val lon: Double
)

fun getWorldCities(): List<Weather> {
    return listOf(
        Weather(City("Лондон", 51.5085300, -0.1257400), 1, 2),
        Weather(City("Токио", 35.6895000, 139.6917100), 3, 4),
        Weather(City("Париж", 48.8534100, 2.3488000), 5, 6),
        Weather(City("Берлин", 52.52000659999999, 13.404953999999975), 7, 8),
        Weather(City("Рим", 41.9027835, 12.496365500000024), 9, 10),
        Weather(City("Минск", 53.90453979999999, 27.561524400000053), 11, 12),
        Weather(City("Стамбул", 41.0082376, 28.97835889999999), 13, 14),
        Weather(City("Вашингтон", 38.9071923, -77.03687070000001), 15, 16),
        Weather(City("Киев", 50.4501, 30.523400000000038), 17, 18),
        Weather(City("Пекин", 39.90419989999999, 116.40739630000007), 19, 20)
    )
}

fun getRussianCities(): List<Weather> {
    return listOf(
        Weather(City("Москва", 55.755826, 37.617299900000035), 1, 2),
        Weather(City("Санкт-Петербург", 59.9342802, 30.335098600000038), 3, 3),
        Weather(City("Новосибирск", 55.00835259999999, 82.93573270000002), 5, 6),
        Weather(City("Екатеринбург", 56.83892609999999, 60.60570250000001), 7, 8),
        Weather(City("Нижний Новгород", 56.2965039, 43.936059), 9, 10),
        Weather(City("Казань", 55.8304307, 49.06608060000008), 11, 12),
        Weather(City("Челябинск", 55.1644419, 61.4368432), 13, 14),
        Weather(City("Омск", 54.9884804, 73.32423610000001), 15, 16),
        Weather(City("Ростов-на-Дону", 47.2357137, 39.701505), 17, 18),
        Weather(City("Уфа", 54.7387621, 55.972055400000045), 19, 20)
    )
}

fun getUSACities(): List<Weather> {
    return listOf(
        Weather(City("Вашингтон", 38.8951, -77.0364), 1, 2),
        Weather(City("Нью-Йорк", 40.7143, -74.006), 3, 3),
        Weather(City("Лос-Анджелес", 34.0522,-118.244), 5, 6),
        Weather(City("Чикаго", 41.85, -87.65), 7, 8),
        Weather(City("Лас-Вегас", 36.175, -115.137), 9, 10),
    )
}

fun getDefaultCity() = City("Москва", 55.755826, 37.617299900000035)

/*
temp	Температура (°C).	Число
feels_like	Ощущаемая температура (°C).	Число
temp_water	Температура воды (°C). Параметр возвращается для населенных пунктов, где данная информация актуальна.	Число
icon	Код иконки погоды. Иконка доступна по адресу https://yastatic.net/weather/i/icons/funky/dark/<значение из поля icon>.svg.	Строка


condition	Код расшифровки погодного описания. Возможные значения:
clear — ясно.
partly-cloudy — малооблачно.
cloudy — облачно с прояснениями.
overcast — пасмурно.
drizzle — морось.
light-rain — небольшой дождь.
rain — дождь.
moderate-rain — умеренно сильный дождь.
heavy-rain — сильный дождь.
continuous-heavy-rain — длительный сильный дождь.
showers — ливень.
wet-snow — дождь со снегом.
light-snow — небольшой снег.
snow — снег.
snow-showers — снегопад.
hail — град.
thunderstorm — гроза.
thunderstorm-with-rain — дождь с грозой.
thunderstorm-with-hail — гроза с градом.
Строка


wind_speed	Скорость ветра (в м/с).	Число
wind_gust	Скорость порывов ветра (в м/с).	Число


wind_dir	Направление ветра. Возможные значения:
«nw» — северо-западное.
«n» — северное.
«ne» — северо-восточное.
«e» — восточное.
«se» — юго-восточное.
«s» — южное.
«sw» — юго-западное.
«w» — западное.
«с» — штиль.
Строка

pressure_mm	Давление (в мм рт. ст.).	Число
pressure_pa	Давление (в гектопаскалях).	Число
humidity	Влажность воздуха (в процентах).	Число
daytime	Светлое или темное время суток. Возможные значения:
«d» — светлое время суток.
«n» — темное время суток.
Строка
polar	Признак того, что время суток, указанное в поле daytime, является полярным.	Логический
season	Время года в данном населенном пункте. Возможные значения:
«summer» — лето.
«autumn» — осень.
«winter» — зима.
«spring» — весна.
Строка
obs_time	Время замера погодных данных в формате Unixtime.	Число
 */