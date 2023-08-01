package com.example.flightsearch.data

data class Flight(
    val departureAirport: Airport,
    val arrivalAirport: Airport,
    val isFavourite: Boolean = false
)
