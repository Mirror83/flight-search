package com.example.flightsearch

import android.app.Application
import com.example.flightsearch.data.FlightSearchAppContainer
import com.example.flightsearch.data.FlightSearchAppDataContainer

class FlightSearchApplication: Application() {
    lateinit var container: FlightSearchAppContainer
    override fun onCreate() {
        super.onCreate()
        container = FlightSearchAppDataContainer(this)
    }
}