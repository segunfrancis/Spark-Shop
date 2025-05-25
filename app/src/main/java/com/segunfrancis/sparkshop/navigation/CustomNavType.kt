package com.segunfrancis.sparkshop.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.segunfrancis.sparkshop.data.remote.Product
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object CustomNavType {
    val productType = object : NavType<Product>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): Product? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): Product {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun put(bundle: Bundle, key: String, value: Product) {
            bundle.putString(key, Json.encodeToString(value))
        }

        override fun serializeAsValue(value: Product): String {
            return Uri.encode(Json.encodeToString(value))
        }
    }
}
