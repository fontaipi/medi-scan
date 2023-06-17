package com.fontaipi.mediscan.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.fontaipi.mediscan.feature.details.detailsRoute
import com.fontaipi.mediscan.feature.scanner.navigateToScannerRoute
import com.fontaipi.mediscan.feature.scanner.scannerRoute
import com.fontaipi.mediscan.feature.search.navigateToSearch
import com.fontaipi.mediscan.feature.search.searchRoute
import com.fontaipi.mediscan.navigation.TopLevelDestination

@Composable
fun rememberMediScanState(
    navController: NavHostController = rememberNavController()
): MediScanAppState {
    return remember(navController) {
        MediScanAppState(navController)
    }
}

@Stable
class MediScanAppState(
    val navController: NavHostController
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            scannerRoute -> TopLevelDestination.SCANNER
            searchRoute -> TopLevelDestination.SEARCH
            else -> null
        }

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.values().asList()

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val topLevelNavOptions = navOptions {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }

        when (topLevelDestination) {
            TopLevelDestination.SCANNER -> navController.navigateToScannerRoute(topLevelNavOptions)
            TopLevelDestination.SEARCH -> navController.navigateToSearch(topLevelNavOptions)
            TopLevelDestination.HISTORY -> {}
        }
    }

    val currentDestinationTitleText: String
        @Composable get() = with(currentDestination?.route) {
            when {
                equals(scannerRoute) -> TopLevelDestination.SCANNER.titleText
                equals(searchRoute) -> TopLevelDestination.SEARCH.titleText
                this?.contains(detailsRoute) == true -> "Détail du médicament"
                else -> ""
            }
        }

    fun onBackClick() {
        navController.popBackStack()
    }
}
