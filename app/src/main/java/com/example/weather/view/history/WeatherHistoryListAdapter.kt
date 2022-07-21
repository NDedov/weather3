package com.example.weather.view.history

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.databinding.FragmentWeatherHistoryListRecyclerItemBinding
import com.example.weather.domain.Weather
import java.text.SimpleDateFormat

class WeatherHistoryListAdapter(
    var dataList: List<Weather>,
    private val callback: OnWeatherHistoryClick
) : RecyclerView.Adapter<WeatherHistoryListAdapter.WeatherViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding =
            FragmentWeatherHistoryListRecyclerItemBinding.inflate(LayoutInflater.from(parent.context))
        return WeatherViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class WeatherViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("SimpleDateFormat")
        fun bind(weather: Weather) {
            val binding = FragmentWeatherHistoryListRecyclerItemBinding.bind(itemView)
            binding.cityName.text = weather.city.name
            binding.temperatureValue.text = String.format("${weather.temp}°C")

            binding.dateValue.text =
                SimpleDateFormat("yyyy.MM.dd '-' HH:mm:ss z").format(weather.date)

            binding.root.setOnClickListener {
                callback.onItemClick(weather)
            }
            binding.deleteButton.setOnClickListener {
                callback.onDeleteClick(weather)
            }
        }
    }
}