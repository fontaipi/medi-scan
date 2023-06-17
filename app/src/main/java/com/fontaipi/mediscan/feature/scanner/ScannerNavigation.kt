package com.fontaipi.mediscan.feature.scanner

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val scannerRoute = "scanner_route"

fun NavController.navigateToScannerRoute(navOptions: NavOptions? = null) {
    this.navigate(scannerRoute, navOptions)
}

fun NavGraphBuilder.scannerRoute(
    onBarcodeScan: (String) -> Unit
) {
    composable(route = scannerRoute) {
        ScannerRoute(onBarcodeScan)
    }
}
