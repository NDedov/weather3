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
import com.example.weather.utils.snackBarWithAction
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

        with(binding) {
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