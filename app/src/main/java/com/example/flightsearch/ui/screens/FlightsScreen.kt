package com.example.flightsearch.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flightsearch.R
import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.Flight
import com.example.flightsearch.data.FlightSearchData

@Composable
fun FlightsScreen(
    airport: Airport,
    flights: List<Flight>,
    favouriteFlights: List<Flight>,
    toggleFavourite: (Flight) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text("Flights from ${airport.iataCode}", modifier = Modifier.padding(8.dp))
        LazyColumn {
            items(items = flights, { it.arrivalAirport.iataCode }) {
                FlightCard(
                    flight = it,
                    isFavourite = favouriteFlights.contains(it),
                    toggleFavourite = toggleFavourite,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                )
            }
        }
    }

}

@Composable
fun FlightCard(
    flight: Flight,
    isFavourite: Boolean,
    toggleFavourite: (Flight) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier.padding(4.dp)
        ) {
            Column {
                Column {
                    Text("Departure")
                    AirportRow(airport = flight.departureAirport)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Column {
                    Text("Arrival")
                    AirportRow(airport = flight.arrivalAirport)
                }
            }
            IconToggleButton(checked = isFavourite, onCheckedChange = { toggleFavourite(flight) }) {
                if (isFavourite) {
                    Icon(
                        painter = painterResource(id = R.drawable.favourite_filled),
                        contentDescription = "Remove from favourites"
                    )
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.favourite_outlined),
                        contentDescription = "Add to favourites"
                    )
                }
            }


        }
    }
}

@Preview
@Composable
fun FlightsScreenPreview() {
    val airport = FlightSearchData.airportList[0]
    val flights: MutableList<Flight> = mutableListOf()
    FlightSearchData.airportList.forEach {
        if (airport != it) flights.add(Flight(airport, it))
    }
    FlightsScreen(
        airport = airport,
        favouriteFlights = emptyList(),
        toggleFavourite = {},
        flights = flights
    )
}