package com.example.weather.view.detailed

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weather.databinding.FragmentWeatherDetailedBinding
import com.example.weather.domain.Weather
import com.example.weather.utils.conditionTranslate
import com.example.weather.viewmodel.detailed.DetailedAppState
import com.example.weather.viewmodel.detailed.DetailedViewModel

class WeatherDetailedFragment : Fragment() {

    private var _binding: FragmentWeatherDetailedBinding? = null
    private val binding: FragmentWeatherDetailedBinding
        get() {
            return _binding!!
        }

    private lateinit var weatherLocal: Weather

    private val viewModel by lazy {
        ViewModelProvider(this).get(DetailedViewModel::class.java)
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

        val weather = arguments?.let { arg ->
            arg.getParcelable<Weather>(BUNDLE_WEATHER_EXTRA)
        }

        weather?.let { weatherLocal ->
            this.weatherLocal = weatherLocal
            viewModel.getWeather(weatherLocal.city.lat, weatherLocal.city.lon)
            viewModel.getLiveData().observe(viewLifecycleOwner) {
                renderData(it)
            }
        }

    }


    private fun renderData(detailedAppState: DetailedAppState) {
        when (detailedAppState) {
            is DetailedAppState.Loading -> {}
            is DetailedAppState.Error -> {}
            is DetailedAppState.Success -> {
                with(binding) {
                    val weatherDTO = detailedAppState.weather
                    cityName.text = weatherLocal.city.name
                    tempValue.text = String.format("${weatherDTO.fact.temp}°C")
                    feelsLikeValue.text = String.format("${weatherDTO.fact.feelsLike}°C")
                    conditionValue.text = conditionTranslate(weatherDTO.fact.condition)
                    cityCoordinates.text =
                        String.format("${weatherLocal.city.lat}/${weatherLocal.city.lon}")
                }
            }
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

