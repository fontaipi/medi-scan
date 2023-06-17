package com.fontaipi.mediscan.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.fontaipi.mediscan.navigation.MediScanItem
import com.fontaipi.mediscan.navigation.MediScanNavHost
import com.fontaipi.mediscan.navigation.TopLevelDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediScanApp(
    appState: MediScanAppState = rememberMediScanState(),
    syncFromNetwork: () -> Unit
) {
    Scaffold(
        bottomBar = {
            NavigationBar {
                appState.topLevelDestinations.forEach { destination ->
                    val selected =
                        appState.currentDestination.isTopLevelDestinationInHierarchy(destination)
                    MediScanItem(
                        selected = selected,
                        onClick = { appState.navigateToTopLevelDestination(destination) },
                        icon = {
                            Icon(
                                imageVector = destination.unselectedIcon,
                                contentDescription = null
                            )
                        },
                        selectedIcon = {
                            Icon(
                                imageVector = destination.selectedIcon,
                                contentDescription = null
                            )
                        },
                        label = { Text(destination.iconText) }
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            CenterAlignedTopAppBar(
                title = { Text(text = appState.currentDestinationTitleText) },
                navigationIcon = {
                    if (appState.currentTopLevelDestination == null) {
                        IconButton(onClick = appState::onBackClick) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = null
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = syncFromNetwork) {
                        Icon(imageVector = Icons.Default.Sync, contentDescription = null)
                    }
                }
            )
            MediScanNavHost(appState = appState)
        }
    }
}

fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination): Boolean {
    val destinationName = destination.name
    val isDestinationInHierarchy = this?.hierarchy?.any {
        val routeName = it.route
        val isMatch = routeName?.contains(destinationName, true) ?: false
        println("Route: $routeName, Destination: $destinationName, Match: $isMatch")
        isMatch
    } ?: false
    println("IsTopLevelDestinationInHierarchy: $isDestinationInHierarchy")
    return isDestinationInHierarchy
}
