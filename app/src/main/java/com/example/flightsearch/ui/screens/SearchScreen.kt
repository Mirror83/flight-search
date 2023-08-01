package com.example.flightsearch.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.flightsearch.R
import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.Flight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    searchTerm: String,
    updateSearchTerm: (String) -> Unit,
    airports: List<Airport>,
    favouriteFlights: List<Flight>,
    toggleFavourite: (Flight) -> Unit,
    onAirportSelected: (Airport) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        OutlinedTextField(
            value = searchTerm,
            onValueChange = { updateSearchTerm(it) },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.search_icon),
                    contentDescription = null
                )
            },
            shape = RoundedCornerShape(40)
        )

        if (searchTerm.isBlank()) {
            if (favouriteFlights.isEmpty())
                Text(
                    text = "No favourites yet!",
                    textAlign = TextAlign.Center,
                    modifier = modifier.padding(top = 8.dp)
                )
            else {
                LazyColumn {
                    items(favouriteFlights, { it.arrivalAirport.iataCode }) {
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
        } else if (airports.isEmpty()) {
            Text(
                text = "No airports matching the search",
                textAlign = TextAlign.Center,
                modifier = modifier.padding(top = 8.dp)
            )
        } else {
            AirportList(airports = airports, onAirportSelected)
        }
    }
}

@Composable
fun AirportList(
    airports: List<Airport>,
    onAirportSelected: (Airport) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(airports, { it.id }) {
            AirportRow(it,
                modifier
                    .padding(16.dp)
                    .clickable { onAirportSelected(it) })
        }
    }
}

@Composable
fun AirportRow(airport: Airport, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
    ) {
        Text(
            airport.iataCode,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(airport.name)
    }
}