package com.example.weather

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.weather.databinding.ActivityMainBinding
import com.example.weather.utils.CONTENT_PROVIDER_FRAGMENT_TAG
import com.example.weather.utils.MAPS_FRAGMENT_TAG
import com.example.weather.utils.WEATHER_HISTORY_LIST_FRAGMENT_TAG
import com.example.weather.view.contentprovider.ContentProviderFragment
import com.example.weather.view.history.WeatherHistoryListFragment
import com.example.weather.view.maps.MapsFragment
import com.example.weather.view.weatherlist.WeatherListFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var lostConnectionReceiver: LostConnectionReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lostConnectionReceiver = LostConnectionReceiver()

        registerReceiver(
            lostConnectionReceiver,
            IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        )

        if (savedInstanceState == null)
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, WeatherListFragment.newInstance())
                .commit()

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(lostConnectionReceiver)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_screen_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_history -> {
                if (supportFragmentManager.fragments.first().tag != WEATHER_HISTORY_LIST_FRAGMENT_TAG)
                    showFragment(WeatherHistoryListFragment())
            }
            R.id.menu_map -> {
                if (supportFragmentManager.fragments.first().tag != MAPS_FRAGMENT_TAG)
                    showFragment(MapsFragment())
            }
            R.id.menu_contacts -> {
                if (supportFragmentManager.fragments.first().tag != CONTENT_PROVIDER_FRAGMENT_TAG)
                    showFragment(ContentProviderFragment())
            }
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.apply {
            beginTransaction()
                .replace(
                    R.id.container,
                    fragment,
                    WEATHER_HISTORY_LIST_FRAGMENT_TAG
                )
                .addToBackStack("").commitAllowingStateLoss()
        }
    }
}

class LostConnectionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("@@@", " MyBroadCastReceiver ${intent!!.action}")
        Toast.makeText(context, "Изменение параметров соединения!", Toast.LENGTH_SHORT).show()
    }
}