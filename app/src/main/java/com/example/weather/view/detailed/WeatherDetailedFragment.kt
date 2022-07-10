package com.example.weather.view.detailed

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.weather.databinding.FragmentWeatherDetailedBinding
import com.example.weather.domain.Weather
import com.example.weather.model.DTO.WeatherDTO
import com.example.weather.utils.WeatherLoader
import com.example.weather.utils.conditionTranslate
import com.example.weather.utils.snackBarWithAction

class WeatherDetailedFragment : Fragment() {

    private var _binding: FragmentWeatherDetailedBinding? = null
    private val binding: FragmentWeatherDetailedBinding
        get() {
            return _binding!!
        }

    private lateinit var weather: Weather

    private val onResponseListener = object : OnResponse {
        override fun onResponse(weatherDto: WeatherDTO) {
            requireActivity().runOnUiThread {
                renderData(weather.apply {
                    feelsLike = weatherDto.fact.feelsLike
                    temp = weatherDto.fact.temp
                    condition = weatherDto.fact.condition
                })
            }
        }

        @RequiresApi(Build.VERSION_CODES.N)
        override fun onFailResponse(message: String) {
            requireActivity().runOnUiThread {
                binding.root.snackBarWithAction(
                    "Ошибка загрузки!\n$message",
                    "Повторить",
                    { loadWeather() }, maxLines = 5
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherDetailedBinding.inflate(inflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        weather = arguments?.getParcelable(BUNDLE_WEATHER_EXTRA)!!

        loadWeather()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun loadWeather() {
        weather.apply {
            WeatherLoader.request(city.lat, city.lon, onResponseListener)
        }
    }

    private fun renderData(weather: Weather) {
        with(binding) {
            cityName.text = weather.city.name
            tempValue.text = String.format("${weather.temp}°C")
            feelsLikeValue.text = String.format("${weather.feelsLike}°C")
            conditionValue.text = conditionTranslate(weather.condition)
            cityCoordinates.text = String.format("${weather.city.lat}/${weather.city.lon}")
        }
    }

    companion object {
        const val BUNDLE_WEATHER_EXTRA = "BUNDLE_WEATHER_EXTRA"
        fun newInstance(weather: Weather): WeatherDetailedFragment {
            return WeatherDetailedFragment().also {
                it.arguments = Bundle().apply {
                    putParcelable(BUNDLE_WEATHER_EXTRA, weather)
                }
            }
        }
    }
}

