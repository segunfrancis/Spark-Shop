package com.segunfrancis.sparkshop.navigation

import com.segunfrancis.sparkshop.data.remote.Product
import kotlinx.serialization.Serializable

@Serializable
sealed class NavDestinations {
    @Serializable
    data object Home : NavDestinations()

    @Serializable
    data class Details(val product: Product) : NavDestinations()

    @Serializable
    data object Cart : NavDestinations()

    @Serializable
    data object Checkout : NavDestinations()
}
