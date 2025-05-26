package com.segunfrancis.sparkshop.data.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "product")
data class ProductEntity(
    @PrimaryKey val id: Int,
    val price: Double,
    val title: String,
    val stock: Int,
    val category: String,
    val description: String,
    val image: String,
    val shippingInformation: String,
    val returnPolicy: String,
    val percentageOff: Int
)

@Entity(
    tableName = "rating", foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RatingEntity(
    @PrimaryKey val productId: Int,
    val rate: Double,
    val count: Int
)

@Entity(
    tableName = "dimensions",
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DimensionsEntity(
    @PrimaryKey val productId: Int,
    val width: Double,
    val height: Double,
    val length: Double
)

@Entity(tableName = "cart")
data class CartItemEntity(
    @PrimaryKey val cartItemId: Int = 0,
    val price: Double,
    val image: String,
    val title: String,
    val stock: Int,
    val quantity: Int
)

data class ProductWithDetails(
    @Embedded val product: ProductEntity,
    @Relation(parentColumn = "id", entityColumn = "productId")
    val rating: RatingEntity,
    @Relation(parentColumn = "id", entityColumn = "productId")
    val dimensions: DimensionsEntity
)
