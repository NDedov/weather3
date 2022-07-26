package com.example.weather.view.weatherlist

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weather.R
import com.example.weather.databinding.FragmentWeatherListBinding
import com.example.weather.domain.City
import com.example.weather.domain.Weather
import com.example.weather.model.Location
import com.example.weather.utils.*
import com.example.weather.view.detailed.OnWeatherListItemClick
import com.example.weather.view.detailed.WeatherDetailedFragment
import com.example.weather.viewmodel.citylist.CityListAppState
import com.example.weather.viewmodel.citylist.CityListViewModel
import java.util.*

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

    lateinit var locationManager: LocationManager

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
            userLocationWeatherFAB.setOnClickListener {
                checkPermissionCommon(Manifest.permission.ACCESS_FINE_LOCATION) {
                    getUserLocation()
                }
            }
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
        if (this::locationManager.isInitialized)
            locationManager.removeUpdates(locationListener)

        requireActivity()
            .supportFragmentManager
            .beginTransaction()
            .hide(this)
            .add(
                R.id.container, WeatherDetailedFragment.newInstance(weather)
            ).addToBackStack("").commit()
    }

    private fun getUserLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager =
                requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    20000L,
                    0F,
                    locationListener
                )
            } else {
                val providers = locationManager.getProviders(false)
                providers.forEach {
                    val lastLocation = locationManager.getLastKnownLocation(it)
                    if (lastLocation != null) {
                        getAddress(location = lastLocation)
                    }
                }
            }
        }
    }

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: android.location.Location) {
            Log.d("@@@", "${location.latitude} ${location.longitude}")
            getAddress(location)
        }

        override fun onProviderDisabled(provider: String) {
            Log.d("@@@", "onProviderDisabled")
            super.onProviderDisabled(provider)
        }

        override fun onProviderEnabled(provider: String) {
            Log.d("@@@", "onProviderEnabled")
            super.onProviderEnabled(provider)
        }
    }

    fun getAddress(location: android.location.Location) {
        val geocoder = Geocoder(context, Locale("ru_RU"))
        Thread {
            val address = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            onItemClick(
                Weather(
                    City(
                        address.first().locality,
                        location.latitude,
                        location.longitude
                    )
                )
            )
        }.start()
    }

    private fun permissionRequest(permission: String) {
        requestPermissions(arrayOf(permission), REQUEST_CODE_LOCATION)
    }

    private fun checkPermissionCommon(permission: String, action: () -> Unit) {
        val permResult =
            ContextCompat.checkSelfPermission(requireContext(), permission)
        if (permResult == PackageManager.PERMISSION_GRANTED) {
            action.invoke()
        } else if (shouldShowRequestPermissionRationale(permission)) {
            context?.alertDialogPositiveAction(
                getString(R.string.location_permission_title_text),
                getString(R.string.location_permission_message),
                getString(R.string.permission_positive_text),
                { permissionRequest(permission) },
                getString(R.string.permission_negative_text)
            )
        } else {
            permissionRequest(permission)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_LOCATION) {
            for (pIndex in permissions.indices) {
                if (permissions[pIndex] == Manifest.permission.ACCESS_FINE_LOCATION
                    && grantResults[pIndex] == PackageManager.PERMISSION_GRANTED
                ) {
                    getUserLocation()
                    Log.d("@@@", "Ура")
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}