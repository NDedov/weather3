package com.example.weather.view.maps


import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.weather.R
import com.example.weather.databinding.FragmentMapsUiBinding
import com.example.weather.utils.REQUEST_CODE_LOCATION
import com.example.weather.utils.alertDialogPositiveAction
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class MapsFragment : Fragment() {

    lateinit var map: GoogleMap
    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        val barnaul = LatLng(53.22, 83.45)
        googleMap.addMarker(MarkerOptions().position(barnaul).title(getString(R.string.marker_text)))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(barnaul))

        googleMap.setOnMapLongClickListener { latLng ->
            addMarkerToArray(latLng)
            drawLine()
        }

        googleMap.uiSettings.isZoomControlsEnabled = true
        checkPermissionCommon(Manifest.permission.ACCESS_FINE_LOCATION) {
            googleMap.isMyLocationEnabled = true
        }
        googleMap.uiSettings.isMyLocationButtonEnabled = true
    }


    private var _binding: FragmentMapsUiBinding? = null
    private val binding: FragmentMapsUiBinding
        get() {
            return _binding!!
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

    private fun permissionRequest(permission: String) {
        requestPermissions(arrayOf(permission), REQUEST_CODE_LOCATION)
    }

    @SuppressLint("MissingPermission")
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
                    map.isMyLocationEnabled = true
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsUiBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        binding.buttonSearch.setOnClickListener {
            binding.searchAddress.text.toString().let { searchText ->
                val geocoder = Geocoder(requireContext())
                val result = geocoder.getFromLocationName(searchText, 1)
                if (result.isNotEmpty()){
                    val ln = LatLng(result.first().latitude, result.first().longitude)
                    setMarker(ln, searchText, R.drawable.ic_map_marker)
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(ln, 15f))
                }else{
                    Toast.makeText(requireContext(),"Населенный пункт не найден",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private val markers = mutableListOf<Marker>()
    private fun addMarkerToArray(location: LatLng) {
        val marker = setMarker(location, markers.size.toString(), R.drawable.ic_map_pin)
        markers.add(marker)
    }

    private fun drawLine() {
        val last: Int = markers.size - 1
        if (last >= 1) {
            val previous: LatLng = markers[last - 1].position
            val current: LatLng = markers[last].position
            map.addPolyline(
                PolylineOptions()
                    .add(previous, current)
                    .color(Color.RED)
                    .width(15f)
            )
        }
    }

    private fun setMarker(
        location: LatLng,
        searchText: String,
        resourceId: Int
    ): Marker {
        return map.addMarker(
            MarkerOptions()
                .position(location)
                .title(searchText)
                .icon(BitmapDescriptorFactory.fromResource(resourceId))
        )!!
    }

}