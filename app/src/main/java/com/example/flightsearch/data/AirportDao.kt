package com.example.flightsearch.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
@Dao
interface AirportDao {
    @Query("SELECT * FROM airport" +
            " WHERE name LIKE :searchTerm OR iata_code LIKE :searchTerm" +
            " ORDER BY name")
    suspend fun getMatchingAirports(searchTerm: String): List<Airport>

    @Query("SELECT * FROM airport WHERE id != :departureAirportId order BY name")
    suspend fun getArrivalAirportsFor(departureAirportId: Int) : List<Airport>

    @Query("SELECT * FROM airport WHERE iata_code = :iataCode")
    suspend fun getAirportByIataCode(iataCode: String): Airport

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addAirport(airport: Airport)
}