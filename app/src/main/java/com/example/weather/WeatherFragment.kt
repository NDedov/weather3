package com.example.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weather.databinding.FragmentWeatherBinding

class WeatherFragment: Fragment() {

    companion object{
        fun newInstance() = WeatherFragment();
    }

    lateinit var binding: FragmentWeatherBinding
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
        viewModel.liveData.observe(viewLifecycleOwner) {
                t -> Toast.makeText(requireContext(), "РАБОТАЕТ $t", Toast.LENGTH_LONG).show()
        }
        viewModel.sendRequest()

    }




}