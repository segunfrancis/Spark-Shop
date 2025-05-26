package com.segunfrancis.sparkshop.utils

import com.segunfrancis.sparkshop.data.remote.Dimensions
import com.segunfrancis.sparkshop.data.remote.Product
import com.segunfrancis.sparkshop.data.remote.Rating
import java.util.Locale
import kotlin.random.Random

val dummyRating = Rating(
    rate = 12.5,
    count = 8
)

val dummyDimension = Dimensions(
    width = 12.5,
    height = 11.0,
    length = 2.8
)

val shippingInfoList = listOf(
    "Free shipping on orders over $1,000",
    "Standard delivery within 3-5 business days",
    "Express delivery available within 24 hours",
    "Same-day delivery for orders placed before 12 PM",
    "Tracking number provided once your order ships",
    "Nationwide shipping available",
    "Shipping to remote areas may take 7–10 business days",
    "Pickup station option available at checkout",
    "Free shipping for first-time customers",
    "Orders are dispatched within 1-2 business days"
)

val returnPolicyList = listOf(
    "Items can be returned within 7 days of delivery",
    "Return only accepted if item is in original packaging",
    "Return shipping costs are the customer’s responsibility",
    "Refunds processed within 5–10 business days after approval",
    "No returns accepted for clearance or final sale items",
    "Items damaged upon arrival are eligible for free return",
    "Contact support within 48 hours to initiate a return",
    "Exchanges available for size or color issues",
    "Return not accepted for hygiene-related products",
    "Proof of purchase required for all returns"
)

val dummyProduct = Product(
    category = "Electronics",
    description = "High-performance gadget with advanced features",
    rating = dummyRating,
    id = 101,
    image = "https://example.com/product2.jpg",
    price = 299.99,
    stock = 50,
    title = "Premium Smart Device",
    shippingInformation = shippingInfoList.first(),
    returnPolicy = returnPolicyList.first(),
    percentageOff = 23,
    dimensions = dummyDimension
)

fun generateDimension(): Double {
    val random = Random.nextDouble(1.0, 15.0)
    return String.format(Locale.getDefault(), "%.1f", random).toDouble()
}
