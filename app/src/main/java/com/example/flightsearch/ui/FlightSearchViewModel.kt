package com.example.flightsearch.ui

import androidx.lifecycle.ViewModel
import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.Flight
import com.example.flightsearch.data.FlightSearchData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FlightSearchViewModel : ViewModel() {
    private var _uiState: MutableStateFlow<FlightSearchUiState> =
        MutableStateFlow(FlightSearchUiState())

    val uiState = _uiState.asStateFlow()

    fun matchAirports(searchTerm: String) {
        val airportList: List<Airport> =
            with(_uiState.value.searchTerm) {
                if (this == "") emptyList() else
                    FlightSearchData.airportList.filter { airport ->
                        airport.name.contains(this, ignoreCase = true) || airport.iataCode.contains(
                            this, ignoreCase = true
                        )
                    }
            }
        _uiState.value = _uiState.value.copy(
            searchTerm = searchTerm,
            matchedAirports = airportList
        )
    }

    fun selectAirport(airport: Airport) {
        _uiState.value = _uiState.value.copy(
            selectedAirport = airport,
            flights = populateFlights(airport)
        )
    }

    fun toggleFavourite(flight: Flight) {
        _uiState.value = _uiState.value.copy(
            favouriteFlights = if (_uiState.value.favouriteFlights.contains(flight))
                _uiState.value.favouriteFlights.minus(flight)
            else _uiState.value.favouriteFlights.plus(flight)
        )
    }

    private fun populateFlights(airport: Airport): List<Flight> {
        val flights: MutableList<Flight> = mutableListOf()
        FlightSearchData.airportList.forEach {
            if (airport != it) flights.add(Flight(airport, it))
        }

        return flights
    }


}

data class FlightSearchUiState(
    val selectedAirport: Airport? = null,
    val searchTerm: String = "",
    val matchedAirports: List<Airport> = emptyList(),
    val flights: List<Flight> = emptyList(),
    val favouriteFlights: List<Flight> = emptyList()
)