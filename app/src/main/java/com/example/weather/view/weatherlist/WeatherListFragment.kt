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
import com.google.android.material.snackbar.Snackbar

class WeatherListFragment: Fragment(), OnWeatherListItemClick {

    companion object{
        fun newInstance() = WeatherListFragment()
    }

    private var progressLoadingToShow = true//флаг для отображения загрузки
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
        _binding =FragmentWeatherListBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel =ViewModelProvider(this).get(WeatherViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner) { t -> renderData(t) }
        binding.russianButton.setOnClickListener {
            currentLocation = Location.Russian
            sendRequest(currentLocation)
        }
        binding.usaButton.setOnClickListener {
            currentLocation = Location.USA
            sendRequest(currentLocation)
        }
        binding.worldButton.setOnClickListener {
            currentLocation = Location.World
            sendRequest(currentLocation)
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
                if (appState.loadingOver){
                    progressLoadingToShow = false
                    binding.loadingProgress.visibility = View.GONE
                }
                else{
                    binding.loadingProgress.visibility = View.VISIBLE
                    Thread{
                        while (progressLoadingToShow){
                            binding.loadingProgress.progress++
                            if (binding.loadingProgress.progress == binding.loadingProgress.max)
                                binding.loadingProgress.progress = 0
                            Thread.sleep(5)
                        }
                    }.start()
                }
            }
            is AppState.SuccessMulti -> {
                progressLoadingToShow = false
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