package com.segunfrancis.sparkshop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.segunfrancis.sparkshop.data.remote.Product
import com.segunfrancis.sparkshop.navigation.CustomNavType
import com.segunfrancis.sparkshop.navigation.NavDestinations
import com.segunfrancis.sparkshop.ui.auth.login.LoginScreen
import com.segunfrancis.sparkshop.ui.auth.register.RegisterScreen
import com.segunfrancis.sparkshop.ui.cart.CartScreen
import com.segunfrancis.sparkshop.ui.checkout.CheckoutScreen
import com.segunfrancis.sparkshop.ui.details.DetailsScreen
import com.segunfrancis.sparkshop.ui.home.HomeScreen
import com.segunfrancis.sparkshop.ui.theme.SparkShopTheme
import com.segunfrancis.sparkshop.utils.formatEmailUsername
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.typeOf

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val firebaseAuth = Firebase.auth

        val displayName = formatEmailUsername(firebaseAuth.currentUser?.email)
        val startDestination = if (firebaseAuth.currentUser == null) {
            NavDestinations.Login
        } else {
            NavDestinations.Home(displayName)
        }

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            SparkShopTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = startDestination,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable<NavDestinations.Login> {
                            LoginScreen(onNavigateToRegister = {
                                navController.navigate(NavDestinations.Register)
                            }, onLoginComplete = { name ->
                                navController.navigate(NavDestinations.Home(name)) {
                                    popUpTo(NavDestinations.Login) {
                                        inclusive = true
                                    }
                                }
                            })
                        }
                        composable<NavDestinations.Register> {
                            RegisterScreen(
                                onNavigateToLogin = { navController.navigateUp() },
                                onRegisterComplete = { name ->
                                    navController.navigate(NavDestinations.Home(name)) {
                                        popUpTo(NavDestinations.Register) {
                                            inclusive = true
                                        }
                                    }
                                })
                        }
                        composable<NavDestinations.Home> {
                            val argument = it.toRoute<NavDestinations.Home>()
                            HomeScreen(
                                title = argument.displayName,
                                onCartClick = { navController.navigate(NavDestinations.Cart) },
                                onProductClick = { product ->
                                    navController.navigate(NavDestinations.Details(product))
                                })
                        }
                        composable<NavDestinations.Details>(typeMap = mapOf(typeOf<Product>() to CustomNavType.productType)) {
                            val argument = it.toRoute<NavDestinations.Details>()
                            DetailsScreen(
                                product = argument.product,
                                onBack = { navController.navigateUp() }
                            )
                        }
                        composable<NavDestinations.Cart> {
                            CartScreen(onBack = { navController.navigateUp() }, onCheckout = {
                                navController.navigate(
                                    NavDestinations.Checkout
                                )
                            })
                        }
                        composable<NavDestinations.Checkout> {
                            CheckoutScreen(
                                onBack = { navController.navigateUp() },
                                onCheckout = {
                                    navController.navigate(NavDestinations.Home(displayName)) {
                                        popUpTo(NavDestinations.Home::class) {
                                            inclusive = true
                                        }
                                    }
                                })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SparkShopTheme {
        Greeting("Android")
    }
}
