package com.segunfrancis.sparkshop.utils

import com.segunfrancis.sparkshop.data.remote.Dimensions
import com.segunfrancis.sparkshop.data.remote.Meta
import com.segunfrancis.sparkshop.data.remote.Product
import com.segunfrancis.sparkshop.data.remote.Review

val dummyDimensions = Dimensions(
    depth = 12.5,
    height = 8.2,
    width = 15.0
)

val dummyMeta = Meta(
    barcode = "123456789012",
    createdAt = "2023-05-15T10:30:00Z",
    qrCode = "QR123456",
    updatedAt = "2023-05-20T14:45:00Z"
)

val dummyReviews = listOf(
    Review(
        comment = "Great product! Works perfectly.",
        date = "2023-06-01",
        rating = 5,
        reviewerEmail = "user1@example.com",
        reviewerName = "John Doe"
    ),
    Review(
        comment = "Average quality",
        date = "2023-06-10",
        rating = 3,
        reviewerName = "Jane Smith"
    )
)

val dummyProduct = Product(
    availabilityStatus = "In Stock",
    brand = "TechMaster",
    category = "Electronics",
    description = "High-performance gadget with advanced features",
    dimensions = dummyDimensions,
    discountPercentage = 15.0,
    id = 101,
    images = listOf(
        "https://example.com/product1.jpg",
        "https://example.com/product2.jpg"
    ),
    meta = dummyMeta,
    minimumOrderQuantity = 1,
    price = 299.99,
    rating = 4.5,
    returnPolicy = "30-day return policy",
    reviews = dummyReviews,
    shippingInformation = "Free shipping on orders over $50",
    sku = "TM-101-ELEC",
    stock = 50,
    tags = listOf("gadget", "wireless", "2023"),
    thumbnail = "https://example.com/product-thumb.jpg",
    title = "Premium Smart Device",
    warrantyInformation = "2-year manufacturer warranty",
    weight = 350
)
