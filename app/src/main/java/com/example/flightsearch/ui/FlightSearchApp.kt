package com.example.flightsearch.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.flightsearch.R
import com.example.flightsearch.ui.screens.FlightsScreen
import com.example.flightsearch.ui.screens.SearchScreen

enum class FlightSearchAppScreens(val title: String) {
    SEARCH(title = "Flight Search"),
    FLIGHTS(title = "Flights from")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightSearchApp(
    viewModel: FlightSearchViewModel = viewModel(factory = FlightSearchViewModel.Factory)
) {
    val favoriteFlights by viewModel.favoriteFlights.collectAsState()

    val navHostController = rememberNavController()
    val backStackEntry by navHostController.currentBackStackEntryAsState()
    val currentScreen = FlightSearchAppScreens.valueOf(
        backStackEntry?.destination?.route ?: FlightSearchAppScreens.FLIGHTS.name
    )

    Scaffold(
        topBar = {
            FlightSearchAppBar(
                currentScreen = currentScreen,
                selectedAirportCode = viewModel.selectedAirport?.iataCode,
                canNavigateBack = navHostController.previousBackStackEntry != null,
                onNavigateBack = { navHostController.navigateUp() },
            )
        }
    ) { paddingValues ->
        NavHost(
            navController = navHostController,
            startDestination = FlightSearchAppScreens.SEARCH.name
        ) {
            composable(route = FlightSearchAppScreens.SEARCH.name) {
                SearchScreen(
                    searchTerm = viewModel.searchTerm,
                    updateSearchTerm = viewModel::matchAirports,
                    airports = viewModel.matchedAirports,
                    favouriteFlights = favoriteFlights,
                    toggleFavourite = viewModel::toggleFavorite,
                    onAirportSelected = {
                        viewModel.selectAirport(it)
                        navHostController.navigate(FlightSearchAppScreens.FLIGHTS.name)
                    },
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                )
            }
            composable(route = FlightSearchAppScreens.FLIGHTS.name) {
                FlightsScreen(
                    flights = viewModel.flights,
                    toggleFavourite = viewModel::toggleFavorite,
                    favouriteFlights = favoriteFlights,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightSearchAppBar(
    currentScreen: FlightSearchAppScreens,
    selectedAirportCode: String?,
    canNavigateBack: Boolean,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            if (currentScreen == FlightSearchAppScreens.FLIGHTS)
                Text("${currentScreen.title} $selectedAirportCode")
            else Text(text = currentScreen.title)
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = { onNavigateBack() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_back),
                        contentDescription = "Back"
                    )
                }
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary

        ),
        modifier = modifier
    )
}
