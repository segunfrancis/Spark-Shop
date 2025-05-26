package com.segunfrancis.sparkshop.navigation

import com.segunfrancis.sparkshop.data.remote.Product
import kotlinx.serialization.Serializable

@Serializable
sealed class NavDestinations {
    @Serializable
    data object Login : NavDestinations()

    @Serializable
    data object Register : NavDestinations()

    @Serializable
    data class Home(val displayName: String?) : NavDestinations()

    @Serializable
    data class Details(val product: Product) : NavDestinations()

    @Serializable
    data object Cart : NavDestinations()

    @Serializable
    data object Checkout : NavDestinations()
}
