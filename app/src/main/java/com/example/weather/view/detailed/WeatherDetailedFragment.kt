package com.example.weather.view.detailed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.weather.databinding.FragmentWeatherDetailedBinding
import com.example.weather.domain.Weather

class WeatherDetailedFragment : Fragment() {

    private var _binding: FragmentWeatherDetailedBinding? = null
    private val binding: FragmentWeatherDetailedBinding
        get() {
            return _binding!!
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val weather = (arguments?.getParcelable<Weather>(BUNDLE_WEATHER_EXTRA))
        if (weather != null)
            renderData(weather)
    }

    private fun renderData(weather: Weather) {
        with (binding){
            cityName.text = weather.city.name
            tempValue.text = String.format("${weather.temp}°C")
            feelsLikeValue.text = String.format("${weather.feelsLike}°C")
            conditionValue.text = weather.condition
            cityCoordinates.text = String.format("${weather.city.lat}/${weather.city.lon}")
        }
    }

    companion object {
        const val BUNDLE_WEATHER_EXTRA = "BUNDLE_WEATHER_EXTRA"
        fun newInstance(weather: Weather): WeatherDetailedFragment {
            return WeatherDetailedFragment().also{
                it.arguments = Bundle().apply{
                    putParcelable(BUNDLE_WEATHER_EXTRA, weather)
                }
            }
        }
    }
}
