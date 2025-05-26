package com.segunfrancis.sparkshop.data.remote


import android.os.Parcelable
import androidx.annotation.Keep
import com.segunfrancis.sparkshop.utils.generateDimension
import com.segunfrancis.sparkshop.utils.returnPolicyList
import com.segunfrancis.sparkshop.utils.shippingInfoList
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlin.random.Random

@Keep
@Parcelize
@Serializable
data class Product(
    val category: String,
    val description: String,
    val rating: Rating,
    val id: Int,
    val image: String,
    val price: Double,
    val title: String,
    val stock: Int = Random.nextInt(30),
    val shippingInformation: String = shippingInfoList[Random.nextInt(shippingInfoList.size)],
    val returnPolicy: String = returnPolicyList[Random.nextInt(returnPolicyList.size)],
    val percentageOff: Int = Random.nextInt(25),
    val dimensions: Dimensions = Dimensions(
        width = generateDimension(),
        height = generateDimension(),
        length = generateDimension()
    )
) : Parcelable

@Keep
@Parcelize
@Serializable
data class Rating(
    val rate: Double,
    val count: Int
) : Parcelable

@Keep
@Parcelize
@Serializable
data class Dimensions(
    val width: Double,
    val height: Double,
    val length: Double
) : Parcelable