package com.example.weather.view.weatherlist

import android.content.Context
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
import com.example.weather.utils.*
import com.example.weather.view.detailed.OnWeatherListItemClick
import com.example.weather.view.detailed.WeatherDetailedFragment
import com.example.weather.viewmodel.citylist.CityListAppState
import com.example.weather.viewmodel.citylist.CityListViewModel

class WeatherListFragment : Fragment(), OnWeatherListItemClick {

    companion object {
        fun newInstance() = WeatherListFragment()
    }

    private var currentLocation: Location = Location.Russian//текущая локация, для перезагрузки
    private var _binding: FragmentWeatherListBinding? = null
    private val binding: FragmentWeatherListBinding
        get() {
            return _binding!!
        }
    private lateinit var viewModel: CityListViewModel

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

        viewModel = ViewModelProvider(this).get(CityListViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner) { t -> renderData(t) }

        getLocationFromSP()

        with(binding) {
            russianButton.setOnClickListener { processLocationButtonClick(Location.Russian) }
            usaButton.setOnClickListener { processLocationButtonClick(Location.USA) }
            worldButton.setOnClickListener { processLocationButtonClick(Location.World) }
        }
        sendRequest(currentLocation)
    }

    private fun getLocationFromSP() {
        val sp = requireActivity().getSharedPreferences(SP_DB_LOCATION, Context.MODE_PRIVATE)
        currentLocation = when (sp.getString(SP_DB_LOCATION_KEY, LOCATION_RUSSIAN)) {
            LOCATION_RUSSIAN -> Location.Russian
            LOCATION_USA -> Location.USA
            LOCATION_WORLD -> Location.World
            else -> {
                Location.Russian
            }
        }
    }

    private fun processLocationButtonClick(location: Location) {
        currentLocation = location
        sendRequest(currentLocation)
        val sp = requireActivity().getSharedPreferences(SP_DB_LOCATION, Context.MODE_PRIVATE)
        sp.edit().apply() {
            putString(
                SP_DB_LOCATION_KEY, when (currentLocation) {
                    Location.Russian -> LOCATION_RUSSIAN
                    Location.USA -> LOCATION_USA
                    Location.World -> LOCATION_WORLD
                }
            )
            apply()
        }
    }

    private fun sendRequest(location: Location) {
        try {
            viewModel.sendRequest(location)
        } catch (ex: IllegalStateException) {
        }
    }

    private fun renderData(appState: CityListAppState) {
        when (appState) {
            is CityListAppState.Error -> {
                binding.root.snackBarWithAction("Ошибка загрузки",
                    "Повторить", { sendRequest(currentLocation) })
            }
            is CityListAppState.Loading -> {
                if (appState.loadingOver)
                    binding.loadingProgress.visibility = View.GONE
                else
                    binding.loadingProgress.visibility = View.VISIBLE
            }
            is CityListAppState.SuccessMulti -> {
                binding.weatherListRecyclerView.adapter =
                    WeatherListAdapter(appState.weatherList, this)
            }
        }
    }

    override fun onItemClick(weather: Weather) {
        requireActivity()
            .supportFragmentManager
            .beginTransaction()
            .hide(this)
            .add(
                R.id.container, WeatherDetailedFragment.newInstance(weather)
            ).addToBackStack("").commit()
    }
}