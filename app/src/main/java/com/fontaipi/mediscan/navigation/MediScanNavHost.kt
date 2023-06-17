package com.fontaipi.mediscan.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.fontaipi.mediscan.feature.details.detailsScreen
import com.fontaipi.mediscan.feature.details.navigateToDetails
import com.fontaipi.mediscan.feature.scanner.scannerRoute
import com.fontaipi.mediscan.feature.search.searchGraph
import com.fontaipi.mediscan.ui.MediScanAppState

@Composable
fun MediScanNavHost(
    appState: MediScanAppState,
    startDestination: String = scannerRoute
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        searchGraph(
            navigateToDrugDetails = { navController.navigateToDetails(it) },
            nestedGraphs = {
                detailsScreen()
            }
        )
        scannerRoute { navController.navigateToDetails(it) }
    }
}
