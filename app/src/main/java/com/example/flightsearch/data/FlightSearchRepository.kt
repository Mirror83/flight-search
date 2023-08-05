package com.example.flightsearch.data

import kotlinx.coroutines.flow.Flow

interface FlightSearchRepository {
    suspend fun getAirportByIataCode(iataCode: String): Airport
    suspend fun getMatchingAirports(searchTerm: String): List<Airport>
    suspend fun addAirport(airport: Airport)
    suspend fun getArrivalAirportsFor(departureAirportId: Int) : List<Airport>

    fun getAllFavoritesStream(): Flow<List<Favorite>>

    fun getFavoriteBy(departureCode: String, arrivalCode: String): Flow<Favorite>
    suspend fun addFavorite(favorite: Favorite)
    suspend fun removeFavorite(favorite: Favorite)

}
