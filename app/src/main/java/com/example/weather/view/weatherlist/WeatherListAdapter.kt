package com.example.weather.view.weatherlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.databinding.FragmentWeatherListRecyclerItemBinding
import com.example.weather.domain.Weather
import com.example.weather.view.detailed.OnWeatherListItemClick


class WeatherListAdapter(
    private val dataList: List<Weather>,
    private val callback: OnWeatherListItemClick
) :
    RecyclerView.Adapter<WeatherListAdapter.WeatherViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding =
            FragmentWeatherListRecyclerItemBinding.inflate(LayoutInflater.from(parent.context))
        return WeatherViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class WeatherViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(weather: Weather) {
            val binding = FragmentWeatherListRecyclerItemBinding.bind(itemView)
            with(binding) {
                cityName.text = weather.city.name
                root.setOnClickListener {
                    callback.onItemClick(weather)
                }
            }
        }
    }
}