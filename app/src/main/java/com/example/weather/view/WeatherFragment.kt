package com.example.weather.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weather.databinding.FragmentWeatherBinding
import com.example.weather.viewmodel.AppState
import com.google.android.material.snackbar.Snackbar

class WeatherFragment: Fragment() {

    companion object{
        fun newInstance() = WeatherFragment();
    }

    private lateinit var binding: FragmentWeatherBinding
    lateinit var viewModel: WeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =FragmentWeatherBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel =ViewModelProvider(this).get(WeatherViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner
        ) { t -> renderData(t) }

        try{
            viewModel.sendRequest()
        }
        catch (ex : IllegalStateException){ }
    }

    private fun renderData(appState: AppState) {
        when (appState){
            is AppState.Error -> {
                Snackbar.make(binding.root, "Ошибка загрузки",Snackbar.LENGTH_LONG)
                    .setAction("Повторить", View.OnClickListener {
                        try{
                            viewModel.sendRequest()
                        }
                        catch (ex : IllegalStateException){ }
                    })
                    .setDuration(8000)
                    .show()
            }
            is AppState.Loading -> {/*TODO HW*/}
            is AppState.Success -> {
                val result = appState.weatherData
                binding.cityName.text = result.city.name
                binding.tempValue.text = result.temp.toString()
                binding.feelsLikeValue.text = result.feelsLike.toString()
                binding.conditionValue.text = result.condition
                binding.cityCoordinates.text = "${result.city.lat}/${result.city.lon}"
            }
        }
    }


}