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
    val thumbnail: String,
    val title: String,
    val weight: Int,
    val stock: Int,
    val rating: Double,
    val category: String,
    val description: String,
    val tags: List<String>,
    val images: List<String>,
    val minimumOrderQuantity: Int,
    val discountPercentage: Double,
    val availabilityStatus: String,
    val sku: String? = null,
    val brand: String? = null,
    val returnPolicy: String? = null,
    val shippingInformation: String? = null,
    val warrantyInformation: String? = null,
)

@Entity(
    tableName = "dimensions",
    foreignKeys = [ForeignKey(
        entity = ProductEntity::class,
        parentColumns = ["id"],
        childColumns = ["productId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class DimensionsEntity(
    @PrimaryKey val productId: Int,
    val width: Double,
    val height: Double,
    val depth: Double
)

@Entity(
    tableName = "meta",
    foreignKeys = [ForeignKey(
        entity = ProductEntity::class,
        parentColumns = ["id"],
        childColumns = ["productId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class MetaEntity(
    @PrimaryKey val productId: Int,
    val barcode: String?,
    val createdAt: String?,
    val qrCode: String?,
    val updatedAt: String?
)

@Entity(
    tableName = "review",
    foreignKeys = [ForeignKey(
        entity = ProductEntity::class,
        parentColumns = ["id"],
        childColumns = ["productId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ReviewEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productId: Int,
    val comment: String?,
    val date: String?,
    val rating: Int,
    val reviewerEmail: String?,
    val reviewerName: String?
)

@Entity(tableName = "cart")
data class CartItemEntity(
    @PrimaryKey val cartItemId: Int = 0,
    val price: Double,
    val thumbnail: String,
    val title: String,
    val stock: Int,
    val quantity: Int
)

data class ProductWithDetails(
    @Embedded val product: ProductEntity,
    @Relation(parentColumn = "id", entityColumn = "productId")
    val dimensions: DimensionsEntity?,
    @Relation(parentColumn = "id", entityColumn = "productId")
    val meta: MetaEntity?,
    @Relation(parentColumn = "id", entityColumn = "productId")
    val review: List<ReviewEntity?>
)
