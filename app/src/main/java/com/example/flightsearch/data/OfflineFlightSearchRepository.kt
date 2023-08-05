package com.example.flightsearch.data

import kotlinx.coroutines.flow.Flow

class OfflineFlightSearchRepository(
    private val airportDao: AirportDao,
    private val favoriteDao: FavoriteDao
) : FlightSearchRepository {
    override suspend fun getAirportByIataCode(iataCode: String): Airport =
        airportDao.getAirportByIataCode(iataCode)

    override suspend fun getMatchingAirports(searchTerm: String): List<Airport> =
        airportDao.getMatchingAirports(searchTerm)

    override suspend fun addAirport(airport: Airport) = airportDao.addAirport(airport)

    override suspend fun getArrivalAirportsFor(departureAirportId: Int): List<Airport> =
        airportDao.getArrivalAirportsFor(departureAirportId)

    override fun getAllFavoritesStream(): Flow<List<Favorite>> = favoriteDao.getAllFavorites()

    override fun getFavoriteBy(departureCode: String, arrivalCode: String): Flow<Favorite> =
        favoriteDao.getFavoriteBy(departureCode, arrivalCode)

    override suspend fun addFavorite(favorite: Favorite) = favoriteDao.addFavorite(favorite)

    override suspend fun removeFavorite(favorite: Favorite) = favoriteDao.removeFavorite(favorite)

}