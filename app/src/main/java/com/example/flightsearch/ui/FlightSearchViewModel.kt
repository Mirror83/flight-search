package com.example.flightsearch.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.flightsearch.FlightSearchApplication
import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.Favorite
import com.example.flightsearch.data.Flight
import com.example.flightsearch.data.FlightSearchRepository
import com.example.flightsearch.data.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FlightSearchViewModel(
    private val flightSearchRepository: FlightSearchRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    var searchTerm by mutableStateOf("")

    var selectedAirport: Airport? by mutableStateOf(null)

    var matchedAirports: List<Airport> by mutableStateOf(emptyList())

    var flights: List<Flight> by mutableStateOf(emptyList())

    val favoriteFlights: StateFlow<List<Flight>> =
        flightSearchRepository.getAllFavoritesStream().map { favorites ->
            favorites.map {
                Flight(
                    departureAirport =
                    flightSearchRepository.getAirportByIataCode(it.departureCode),
                    arrivalAirport =
                    flightSearchRepository.getAirportByIataCode(it.destinationCode),
                )
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            searchTerm = userPreferencesRepository.searchString.first()
            matchedAirports = flightSearchRepository
                .getMatchingAirports(
                    "%${searchTerm}%"
                )
        }
    }

    fun matchAirports(userSearchTerm: String) {
        searchTerm = userSearchTerm
        viewModelScope.launch {
            launch {
                matchedAirports = flightSearchRepository
                    .getMatchingAirports("%${searchTerm}%")
            }
            launch {
                userPreferencesRepository.saveUserSearchString(userSearchTerm)
            }

        }
    }

    fun selectAirport(airport: Airport) {
        selectedAirport = airport
        viewModelScope.launch {
            populateFlights(airport)
        }
    }

    fun toggleFavorite(flight: Flight) {
        viewModelScope.launch {
            val favorite = flightSearchRepository.getFavoriteBy(
                flight.departureAirport.iataCode,
                flight.arrivalAirport.iataCode
            ).firstOrNull()

            if (favorite != null)
                flightSearchRepository.removeFavorite(favorite)
            else
                flightSearchRepository.addFavorite(
                    flight.toFavouriteWithoutIndex()
                )

            Log.d("FAV_FLIGHTS", favoriteFlights.value.toString())
        }
    }

    private suspend fun populateFlights(airport: Airport) {
        flights = flightSearchRepository.getArrivalAirportsFor(airport.id).map {
            it.toFlight(airport)
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
        val Factory = viewModelFactory {
            initializer {
                FlightSearchViewModel(getFlightSearchRepository(), getUserPreferencesRepository())
            }
        }
    }


}

fun CreationExtras.getFlightSearchRepository(): FlightSearchRepository {
    val application = this[APPLICATION_KEY] as FlightSearchApplication
    return application.container.flightSearchRepository
}

fun CreationExtras.getUserPreferencesRepository(): UserPreferencesRepository {
    val application = this[APPLICATION_KEY] as FlightSearchApplication
    return application.container.userPreferencesRepository
}

fun Airport.toFlight(departureAirport: Airport, favouriteId: Int? = null): Flight =
    Flight(departureAirport, this, favouriteId)

fun Flight.toFavouriteWithoutIndex(): Favorite = Favorite(
    departureCode = this.departureAirport.iataCode,
    destinationCode = this.arrivalAirport.iataCode
)