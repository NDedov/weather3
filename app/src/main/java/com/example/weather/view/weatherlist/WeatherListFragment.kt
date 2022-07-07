package com.example.weather.view.weatherlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weather.R
import com.example.weather.databinding.FragmentWeatherListBinding
import com.example.weather.domain.Weather
import com.example.weather.model.Location
import com.example.weather.utils.SNACK_BAR_LONG_DURATION
import com.example.weather.view.detailed.OnWeatherListItemClick
import com.example.weather.view.detailed.WeatherDetailedFragment
import com.example.weather.viewmodel.AppState
import com.example.weather.viewmodel.WeatherViewModel
import com.google.android.material.snackbar.Snackbar

class WeatherListFragment: Fragment(), OnWeatherListItemClick {

    companion object{
        fun newInstance() = WeatherListFragment()
    }

    private var currentLocation:Location = Location.Russian//текущая локация, для перезагрузки
    private var _binding: FragmentWeatherListBinding?= null
    private val binding: FragmentWeatherListBinding
    get() {
        return _binding!!
    }
    private lateinit var viewModel: WeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherListBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner) { t -> renderData(t) }

        with (binding){
            russianButton.setOnClickListener {
                currentLocation = Location.Russian
                sendRequest(currentLocation)
            }
            usaButton.setOnClickListener {
                currentLocation = Location.USA
                sendRequest(currentLocation)
            }
            worldButton.setOnClickListener {
                currentLocation = Location.World
                sendRequest(currentLocation)
            }
        }
        sendRequest(currentLocation)
    }

    private fun sendRequest(location: Location) {
        try{
            viewModel.sendRequest(location)
        }
        catch (ex : IllegalStateException){
        }
    }

    private fun renderData(appState: AppState) {
        when (appState){
            is AppState.Error -> {
                Snackbar.make(binding.root, "Ошибка загрузки",Snackbar.LENGTH_LONG)
                    .setAction("Повторить") { sendRequest(currentLocation) }
                    .setDuration(SNACK_BAR_LONG_DURATION)
                    .show()
            }
            is AppState.Loading -> {
                if (appState.loadingOver)
                    binding.loadingProgress.visibility = View.GONE
                else
                    binding.loadingProgress.visibility = View.VISIBLE
            }
            is AppState.SuccessMulti -> {
                binding.weatherListRecyclerView.adapter = WeatherListAdapter(appState.weatherList, this)
            }
            is AppState.SuccessSingle -> TODO()
        }
    }

    override fun onItemClick(weather: Weather) {
        requireActivity()
            .supportFragmentManager
            .beginTransaction()
            .hide(this)
            .add(R.id.container, WeatherDetailedFragment.newInstance(weather)
        ).addToBackStack("").commit()
    }
}