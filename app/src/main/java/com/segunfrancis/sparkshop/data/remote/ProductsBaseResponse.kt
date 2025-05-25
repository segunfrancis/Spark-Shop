package com.segunfrancis.sparkshop.data.remote


import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class ProductsBaseResponse(
    val limit: Int,
    val products: List<Product>,
    val skip: Int,
    val total: Int
)

@Keep
@Parcelize
@Serializable
data class Product(
    val availabilityStatus: String,
    val brand: String? = null,
    val category: String,
    val description: String,
    val dimensions: Dimensions,
    val discountPercentage: Double,
    val id: Int,
    val images: List<String>,
    val meta: Meta,
    val minimumOrderQuantity: Int,
    val price: Double,
    val rating: Double,
    val returnPolicy: String? = null,
    val reviews: List<Review> = emptyList(),
    val shippingInformation: String? = null,
    val sku: String? = null,
    val stock: Int,
    val tags: List<String>,
    val thumbnail: String,
    val title: String,
    val warrantyInformation: String? = null,
    val weight: Int
) : Parcelable

@Keep
@Parcelize
@Serializable
data class Dimensions(
    val depth: Double,
    val height: Double,
    val width: Double
) : Parcelable

@Keep
@Parcelize
@Serializable
data class Meta(
    val barcode: String? = null,
    val createdAt: String? = null,
    val qrCode: String? = null,
    val updatedAt: String? = null
) : Parcelable

@Keep
@Parcelize
@Serializable
data class Review(
    val comment: String? = null,
    val date: String? = null,
    val rating: Int,
    val reviewerEmail: String? = null,
    val reviewerName: String? = null
) : Parcelable
