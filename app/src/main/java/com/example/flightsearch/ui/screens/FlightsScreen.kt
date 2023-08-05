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
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flightsearch.R
import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.Flight

@Composable
fun FlightsScreen(
    flights: List<Flight>,
    favouriteFlights: List<Flight>,
    toggleFavourite: (Flight) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        LazyColumn {
            items(items = flights, { it.arrivalAirport.iataCode }) {
                FlightCard(
                    flight = it,
                    isFavourite = favouriteFlights.contains(it),
                    toggleFavourite = toggleFavourite,
                    modifier = Modifier
                        .padding(dimensionResource(id = R.dimen.padding_small))
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
    Card(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier.padding(dimensionResource(id = R.dimen.padding_small))
        ) {
            Column (modifier = Modifier.weight(3f)){
                Column {
                    Text("Departure", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    AirportRow(airport = flight.departureAirport)
                }
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small)))
                Column {
                    Text("Arrival", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    AirportRow(airport = flight.arrivalAirport)
                }
            }
            IconToggleButton(
                checked = isFavourite,
                onCheckedChange = { toggleFavourite(flight) },) {
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
    val airportList: List<Airport> = listOf(
        Airport(1, "FCO", "Leonardo Da Vinci International Airport", 32781),
        Airport(2, "JKA", "Jomo Kenyatta International Airport", 32781),
        Airport(4, "VIE", "Vienna International Airport", 32781),
        Airport(5, "ATH", "Athens International Airport", 32781),
    )
    val airport = airportList[0]
    val flights: MutableList<Flight> = mutableListOf()
    airportList.forEach {
        if (airport != it) flights.add(Flight(airport, it))
    }
    FlightsScreen(
        favouriteFlights = emptyList(),
        toggleFavourite = {},
        flights = flights
    )
}