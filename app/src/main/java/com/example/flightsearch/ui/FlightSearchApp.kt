package com.example.flightsearch.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.flightsearch.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightSearchApp() {
    Scaffold (
        topBar = { FlightSearchAppBar() }
    ) { paddingValues ->
        FlightSearchContent(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightSearchAppBar(
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },
        modifier = modifier
    )
}

@Composable
fun FlightSearchContent(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Text("Content")
    }
}