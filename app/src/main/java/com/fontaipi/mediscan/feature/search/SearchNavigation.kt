package com.fontaipi.mediscan.feature.search

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.coroutines.ExperimentalCoroutinesApi

const val searchGraphRoutePattern = "search_graph"
const val searchRoute = "search_route"

fun NavController.navigateToSearch(navOptions: NavOptions? = null) {
    this.navigate(searchGraphRoutePattern, navOptions)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun NavGraphBuilder.searchGraph(
    navigateToDrugDetails: (String) -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit
) {
    navigation(
        route = searchGraphRoutePattern,
        startDestination = searchRoute
    ) {
        composable(route = searchRoute) {
            SearchRoute(navigateToDrugDetails = navigateToDrugDetails)
        }
        nestedGraphs()
    }
}
