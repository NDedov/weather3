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
import com.example.weather.databinding.ActivityMainBinding
import com.example.weather.utils.CONTENT_PROVIDER_FRAGMENT_TAG
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
        return when (item.itemId) {
            R.id.menu_history -> {
                supportFragmentManager.apply {
                    beginTransaction()
                        .replace(
                            R.id.container, WeatherHistoryListFragment()
                        ).addToBackStack("").commitAllowingStateLoss()
                }
                true
            }
            R.id.menu_map -> {
                supportFragmentManager.apply {
                    beginTransaction()
                        .replace(
                            R.id.container, MapsFragment()
                        ).addToBackStack("").commitAllowingStateLoss()
                }
                true
            }
            R.id.menu_contacts -> {
                val fragmentA =
                    supportFragmentManager.findFragmentByTag(CONTENT_PROVIDER_FRAGMENT_TAG)
                if (fragmentA == null)
                    supportFragmentManager.apply {
                        beginTransaction()
                            .replace(
                                R.id.container,
                                ContentProviderFragment(),
                                CONTENT_PROVIDER_FRAGMENT_TAG
                            ).addToBackStack("").commitAllowingStateLoss()
                    }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}

class LostConnectionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("@@@", " MyBroadCastReceiver ${intent!!.action}")
        Toast.makeText(context, "Изменение параметров соединения!", Toast.LENGTH_SHORT).show()
    }
}