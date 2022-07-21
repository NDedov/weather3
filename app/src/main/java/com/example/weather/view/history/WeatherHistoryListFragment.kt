package com.example.weather.view.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weather.R
import com.example.weather.databinding.FragmentWeatherHistoryListBinding
import com.example.weather.domain.Weather
import com.example.weather.view.detailed.WeatherDetailedFragment
import com.example.weather.viewmodel.weatherhistorylist.WeatherHistoryListFragmentAppState
import com.example.weather.viewmodel.weatherhistorylist.WeatherHistoryListViewModel

class WeatherHistoryListFragment : Fragment(), OnWeatherHistoryClick {

    companion object {
        fun newInstance() = WeatherHistoryListFragment()
    }

    private var _binding: FragmentWeatherHistoryListBinding? = null
    private val binding: FragmentWeatherHistoryListBinding
        get() {
            return _binding!!
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    lateinit var viewModel: WeatherHistoryListViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherHistoryListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(WeatherHistoryListViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner) { t -> renderData(t) }
        viewModel.getAllHistory()
    }

    private fun renderData(weatherHistoryListFragmentAppState: WeatherHistoryListFragmentAppState) {
        when (weatherHistoryListFragmentAppState) {
            is WeatherHistoryListFragmentAppState.Error -> {
            }
            WeatherHistoryListFragmentAppState.Loading -> {
            }
            is WeatherHistoryListFragmentAppState.SuccessMulti -> {
                binding.historyFragmentRecyclerView.adapter =
                    WeatherHistoryListAdapter(weatherHistoryListFragmentAppState.weatherList, this)
            }
            is WeatherHistoryListFragmentAppState.SuccessDelete -> {
                viewModel.getAllHistory()
            }
        }
    }

    override fun onDeleteClick(weather: Weather) {
        viewModel.deleteHistoryByCity(weather)
    }

    override fun onItemClick(weather: Weather) {
        requireActivity().supportFragmentManager.beginTransaction().hide(this).add(
            R.id.container, WeatherDetailedFragment.newInstance(weather)
        ).addToBackStack("").commit()
    }
}
