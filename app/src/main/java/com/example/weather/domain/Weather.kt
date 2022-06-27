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