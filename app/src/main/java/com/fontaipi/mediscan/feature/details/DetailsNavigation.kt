package com.fontaipi.mediscan.feature.details

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

internal const val cisIdArg = "cisCode"
const val detailsRoute = "details_route"

internal class DetailArgs(val cisCode: String) {
    constructor(savedStateHandle: SavedStateHandle) :
        this(Uri.decode(checkNotNull(savedStateHandle[cisIdArg])))
}

fun NavController.navigateToDetails(cisCode: String) {
    val encodedId = Uri.encode(cisCode)
    this.navigate("$detailsRoute/$encodedId") {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.detailsScreen() {
    composable(
        route = "$detailsRoute/{$cisIdArg}",
        arguments = listOf(
            navArgument(cisIdArg) { type = NavType.StringType }
        )
    ) {
        DetailsRoute()
    }
}
