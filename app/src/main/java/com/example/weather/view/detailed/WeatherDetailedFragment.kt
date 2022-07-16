package com.example.weather.view.detailed

import android.os.Build

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import coil.load
import coil.request.ImageRequest
import com.example.weather.R
import com.example.weather.databinding.FragmentWeatherDetailedBinding
import com.example.weather.domain.Weather
import com.example.weather.utils.conditionTranslate
import com.example.weather.utils.snackBarWithAction
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

        val weather = arguments?.getParcelable<Weather>(BUNDLE_WEATHER_EXTRA)

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
            is DetailedAppState.Loading -> {
                binding.imageCondition.loadGif(R.drawable.loading2)
            }
            is DetailedAppState.Error -> {
                binding.root.snackBarWithAction(
                    detailedAppState.error.message.toString(),
                    "Повторить",
                    {
                        viewModel.getWeather(weatherLocal.city.lat, weatherLocal.city.lon)
                    },
                    maxLines = 5
                )
            }
            is DetailedAppState.Success -> {
                with(binding) {
                    val weatherDTO = detailedAppState.weather
                    cityName.text = weatherLocal.city.name
                    tempValue.text = String.format("${weatherDTO.fact.temp}°C")
                    feelsLikeValue.text = String.format("${weatherDTO.fact.feelsLike}°C")
                    conditionValue.text = conditionTranslate(weatherDTO.fact.condition)
                    cityCoordinates.text =
                        String.format("${weatherLocal.city.lat}/${weatherLocal.city.lon}")
                    imageCondition.loadUrl("https://yastatic.net/weather/i/icons/funky/dark/${weatherDTO.fact.icon}.svg")
                }
            }
        }
    }

    private fun ImageView.loadGif(img: Int) {
        this.load(img, ImageLoader.Builder(requireContext())
            .componentRegistry {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    add(ImageDecoderDecoder(requireContext()))
                } else {
                    add(GifDecoder())
                }
            }
            .build())
    }

    private fun ImageView.loadUrl(url: String) {

        val imageLoader = ImageLoader.Builder(this.context)
            .componentRegistry { add(SvgDecoder(this@loadUrl.context)) }
            .build()

        val request = ImageRequest.Builder(this.context)
            .crossfade(true)
            .crossfade(500)
            .data(url)
            .target(this)
            .build()

        imageLoader.enqueue(request)
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

